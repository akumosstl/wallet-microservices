package io.github.akumosstl.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.TransactionResultDto;
import io.github.akumosstl.commons.type.Types;
import io.github.akumosstl.commons.dto.WalletDto;
import io.github.akumosstl.wallet.dto.FundRequestDto;
import io.github.akumosstl.wallet.dto.TransferRequestDto;
import io.github.akumosstl.wallet.dto.WithdrawRequestDto;
import io.github.akumosstl.wallet.model.Wallet;
import io.github.akumosstl.wallet.repository.WalletRepository;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class WalletService {

    //TODO implement messages log
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    @Setter
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    WalletRepository walletRepository;

    private final KafkaProducerService producerService;

    @Autowired
    public WalletService(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    @Setter
    @Autowired
    private ModelMapper modelMapper;

    public ApiResponseDto fund(FundRequestDto data) {
        double amount =data.getAmount();
        String address = data.getWalletAddress();

        Optional<Wallet> wallet = findByWalletAddress(address);
        var response = new ApiResponseDto();
        var transactionLog = new TransactionResultDto();

        transactionLog.setType(Types.DEPOSIT.name());
        transactionLog.setDate(LocalDateTime.now());
        try {
            transactionLog.setObject(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            //TODO handler it better
            throw new RuntimeException(e);
        }
        if (wallet.isPresent()) {
            Wallet saveFund = addFunds(amount, wallet.get());
            response.setData(saveFund);
            response.setResponseCode(HttpStatus.OK.value());
            response.setResponseMessage("Funding was successful");

            transactionLog.setCode(HttpStatus.OK.value());
            sendToKafkaTopic(transactionLog);
            return response;
        }
        response.setResponseMessage("Wallet Address does not exist");
        response.setResponseCode(HttpStatus.NOT_FOUND.value());

        transactionLog.setCode(HttpStatus.NOT_FOUND.value());
        sendToKafkaTopic(transactionLog);

        return response;
    }

    public ApiResponseDto withdraw(WithdrawRequestDto data) {
        double amount = data.getAmount();
        String address = data.getWalletAddress();

        Optional<Wallet> wallet = findByWalletAddress(address);
        var response = new ApiResponseDto();
        var transactionLog = new TransactionResultDto();

        transactionLog.setType(Types.WITHDRAWAL.name());
        transactionLog.setDate(LocalDateTime.now());
        try {
            transactionLog.setObject(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (wallet.isPresent()) {
            if (amount > wallet.get().getWalletBalance()) {
                response.setResponseCode(HttpStatus.CONFLICT.value());
                response.setResponseMessage("Insufficient Funds");
            } else {
                Wallet withdrawFund = removeFunds(amount, wallet.get());
                response.setData(withdrawFund);
                response.setResponseCode(HttpStatus.OK.value());
                response.setResponseMessage("Withdrawal was successful");

                sendToKafkaTopic(transactionLog);
            }
            return response;
        }
        response.setResponseMessage("Wallet Address does not exist");
        response.setResponseCode(HttpStatus.NOT_FOUND.value());

        transactionLog.setCode(HttpStatus.NOT_FOUND.value());
        sendToKafkaTopic(transactionLog);

        return response;
    }

    public ApiResponseDto transfer(TransferRequestDto data) {
        double amount = data.getAmount();
        String sourceAddress = data.getSourceWalletAddress();
        String destinationAddress = data.getDestinationWalletAddress();

        var transactionLog = new TransactionResultDto();
        Optional<Wallet> source = findByWalletAddress(sourceAddress);
        Optional<Wallet> destination = findByWalletAddress(destinationAddress);

        var response = new ApiResponseDto();

        transactionLog.setType(Types.TRANSFER.name());
        transactionLog.setDate(LocalDateTime.now());
        try {
            transactionLog.setObject(objectMapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (source.isPresent()) {
            if (amount > source.get().getWalletBalance()) {
                response.setResponseCode(HttpStatus.CONFLICT.value());
                response.setResponseMessage("Insufficient Funds");

                transactionLog.setCode(HttpStatus.BAD_REQUEST.value());
                sendToKafkaTopic(transactionLog);

                return response;
            }
        }
        if (source.isPresent()) {
            if (destination.isPresent()) {
                removeFunds(amount, source.get());
                addFunds(amount, source.get());

                response.setResponseMessage("Fund transfer was successful");
                response.setResponseCode(HttpStatus.OK.value());

                transactionLog.setCode(HttpStatus.OK.value());

            } else {
                response.setResponseCode(HttpStatus.NOT_FOUND.value());
                //TODO create a column to act as message hold
                response.setResponseMessage("Credit wallet address is not found");
                transactionLog.setCode(HttpStatus.NOT_FOUND.value());
            }

        } else {
            response.setResponseCode(HttpStatus.NOT_FOUND.value());
            response.setResponseMessage("Debit wallet address is not found");
            transactionLog.setCode(HttpStatus.NOT_FOUND.value());
        }

        sendToKafkaTopic(transactionLog);

        return response;
    }

    //TODO send message to ACCOUNT_TOPIC
    public WalletDto create(Long id) {
        var wallet = new Wallet();

        wallet.setDateCreated(LocalDateTime.now());
        wallet.setAccountId(id);
        wallet.setWalletAddress(UUID.randomUUID().toString());
        wallet.setWalletBalance(0.00);

        return toDto(walletRepository.save(wallet));
    }

    private Optional<Wallet> findByWalletAddress(String walletAddress) {
        return walletRepository.findByWalletAddress(walletAddress);
    }

    private Wallet addFunds(double amount, Wallet wallet) {
        double currentAmount = wallet.getWalletBalance();
        double newAmount = currentAmount + amount;
        wallet.setWalletBalance(newAmount);
        return walletRepository.save(wallet);
    }

    private Wallet removeFunds(double amount, Wallet wallet) {
        double currentAmount = wallet.getWalletBalance();
        double newAmount = currentAmount - amount;
        wallet.setWalletBalance(newAmount);
        return walletRepository.save(wallet);
    }

    private void sendToKafkaTopic(TransactionResultDto message) {
        this.producerService.sendDetails(message);
    }

    private WalletDto toDto(Wallet wallet) {
        return modelMapper.map(wallet, WalletDto.class);
    }

}
