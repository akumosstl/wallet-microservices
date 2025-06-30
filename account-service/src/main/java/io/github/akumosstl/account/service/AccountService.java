package io.github.akumosstl.account.service;

import io.github.akumosstl.account.client.WalletClient;
import io.github.akumosstl.account.model.Account;
import io.github.akumosstl.account.repository.AccountRepository;
import io.github.akumosstl.account.dto.AccountRequestDto;
import io.github.akumosstl.commons.dto.AccountDto;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.WalletDto;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class AccountService {

    @Setter
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WalletClient walletClient;

    @Setter
    @Autowired
    private ModelMapper modelMapper;

    public AccountDto toDto(Account account) {
        return modelMapper.map(account, AccountDto.class);
    }

    public ApiResponseDto createAccount(AccountRequestDto dto) {

        var response = new ApiResponseDto();

        var byEmailOrPhoneAccount = accountRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone());
        if (byEmailOrPhoneAccount.isPresent()) {
            response.setResponseCode(HttpStatus.CONFLICT.value());
            response.setResponseMessage("Account with this phone or email Already exist");
            return response;
        }

        var account = new Account();
        account.setAccountId(dto.getAccountId());
        account.setAccountNo(generateAccountNo());
        account.setEmail(dto.getEmail());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        account.setPhone(dto.getPhone());
        account.setDocument(dto.getDocument());

        var persistedAccount = accountRepository.save(account);
        var accountDto = new AccountDto();
        accountDto.setAccountId(persistedAccount.getAccountId());

        WalletDto persistedWallet = walletClient.getWalletID(toDto(persistedAccount));

        response.setData(persistedWallet);
        response.setResponseCode(HttpStatus.CREATED.value());
        response.setResponseMessage("Account creation was successful");

        return response;
    }

    public static String generateAccountNo() {
        var num = new Random().nextInt(9000000) + 1000000;
        return String.valueOf(num);
    }

}
