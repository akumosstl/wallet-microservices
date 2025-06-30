package io.github.akumosstl.wallet.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.TransactionResultDto;
import io.github.akumosstl.commons.dto.WalletDto;
import io.github.akumosstl.wallet.dto.FundRequestDto;
import io.github.akumosstl.wallet.dto.TransferRequestDto;
import io.github.akumosstl.wallet.dto.WithdrawRequestDto;
import io.github.akumosstl.wallet.model.Wallet;
import io.github.akumosstl.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private ObjectMapper objectMapper;

    private final ModelMapper modelMapper = new ModelMapper();

    private Wallet wallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        walletService = new WalletService(kafkaProducerService);
        walletService.walletRepository = walletRepository;
        walletService.setModelMapper(modelMapper);
        walletService.setObjectMapper(objectMapper);

        wallet = new Wallet();
        wallet.setWalletAddress("test-address");
        wallet.setWalletBalance(100.0);
        wallet.setDateCreated(LocalDateTime.now());
        wallet.setWalletId(1L);
        wallet.setAccountId(999L);
    }

    @Test
    void testFund_successful() throws JsonProcessingException {
        FundRequestDto fundRequest = new FundRequestDto();
        fundRequest.setAmount(50.0);
        fundRequest.setWalletAddress("test-address");

        when(walletRepository.findByWalletAddress("test-address")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenReturn(wallet);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"mock\":\"data\"}");

        ApiResponseDto response = walletService.fund(fundRequest);

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Funding was successful", response.getResponseMessage());
        verify(kafkaProducerService).sendDetails(any(TransactionResultDto.class));
    }

    @Test
    void testFund_walletNotFound() {
        FundRequestDto fundRequest = new FundRequestDto();
        fundRequest.setAmount(50.0);
        fundRequest.setWalletAddress("invalid-address");

        when(walletRepository.findByWalletAddress("invalid-address")).thenReturn(Optional.empty());

        ApiResponseDto response = walletService.fund(fundRequest);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponseCode());
        assertEquals("Wallet Address does not exist", response.getResponseMessage());
    }

    @Test
    void testWithdraw_successful() throws JsonProcessingException {
        WithdrawRequestDto request = new WithdrawRequestDto();
        request.setAmount(40.0);
        request.setWalletAddress("test-address");

        when(walletRepository.findByWalletAddress("test-address")).thenReturn(Optional.of(wallet));
        when(walletRepository.save(any())).thenReturn(wallet);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"mock\":\"data\"}");

        ApiResponseDto response = walletService.withdraw(request);

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Withdrawal was successful", response.getResponseMessage());
        verify(kafkaProducerService).sendDetails(any(TransactionResultDto.class));
    }

    @Test
    void testWithdraw_insufficientFunds() {
        wallet.setWalletBalance(10.0);

        WithdrawRequestDto request = new WithdrawRequestDto();
        request.setAmount(40.0);
        request.setWalletAddress("test-address");

        when(walletRepository.findByWalletAddress("test-address")).thenReturn(Optional.of(wallet));

        ApiResponseDto response = walletService.withdraw(request);

        assertEquals(HttpStatus.CONFLICT.value(), response.getResponseCode());
        assertEquals("Insufficient Funds", response.getResponseMessage());
    }

    @Test
    void testCreateWallet() {
        when(walletRepository.save(any())).thenReturn(wallet);
        WalletDto dto = walletService.create(123L);

        assertNotNull(dto);
        assertEquals("test-address", dto.getWalletAddress());
    }

    @Test
    void testTransfer_bothWalletsExist_successful() throws JsonProcessingException {
        Wallet destination = new Wallet();
        destination.setWalletAddress("destination-address");
        destination.setWalletBalance(20.0);
        destination.setDateCreated(LocalDateTime.now());
        destination.setWalletId(2L);
        destination.setAccountId(888L);

        TransferRequestDto request = new TransferRequestDto();
        request.setAmount(50.0);
        request.setSourceWalletAddress("test-address");
        request.setDestinationWalletAddress("destination-address");

        when(walletRepository.findByWalletAddress("test-address")).thenReturn(Optional.of(wallet));
        when(walletRepository.findByWalletAddress("destination-address")).thenReturn(Optional.of(destination));
        when(walletRepository.save(any())).thenReturn(wallet);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"mock\":\"data\"}");

        ApiResponseDto response = walletService.transfer(request);

        assertEquals(HttpStatus.OK.value(), response.getResponseCode());
        assertEquals("Fund transfer was successful", response.getResponseMessage());
        verify(kafkaProducerService).sendDetails(any(TransactionResultDto.class));
    }

    @Test
    void testTransfer_sourceWalletNotFound() {
        TransferRequestDto request = new TransferRequestDto();
        request.setAmount(50.0);
        request.setSourceWalletAddress("unknown-address");
        request.setDestinationWalletAddress("destination-address");

        when(walletRepository.findByWalletAddress("unknown-address")).thenReturn(Optional.empty());

        ApiResponseDto response = walletService.transfer(request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getResponseCode());
        assertEquals("Debit wallet address is not found", response.getResponseMessage());
    }
}
