package io.github.akumosstl.account.service;

import io.github.akumosstl.account.client.WalletClient;
import io.github.akumosstl.account.dto.AccountRequestDto;
import io.github.akumosstl.account.model.Account;
import io.github.akumosstl.account.repository.AccountRepository;
import io.github.akumosstl.commons.dto.AccountDto;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.WalletDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    private AccountService accountService;
    private AccountRepository accountRepository;
    private WalletClient walletClient;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        accountRepository = mock(AccountRepository.class);
        walletClient = mock(WalletClient.class);
        modelMapper = new ModelMapper();

        accountService = new AccountService();
        accountService.setAccountRepository(accountRepository);
        accountService.walletClient = walletClient;
        accountService.setModelMapper(modelMapper);
    }

    @Test
    public void testCreateAccount_Success() {
        AccountRequestDto requestDto = new AccountRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPhone("1234567890");
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setDocument("123456789");

        when(accountRepository.findByEmailOrPhone("test@example.com", "1234567890"))
                .thenReturn(Optional.empty());

        Account savedAccount = new Account();
        savedAccount.setAccountId(1L);
        savedAccount.setEmail("test@example.com");
        savedAccount.setPhone("1234567890");
        savedAccount.setFirstName("John");
        savedAccount.setLastName("Doe");
        savedAccount.setDocument("123456789");
        savedAccount.setAccountNo("1234567");

        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        WalletDto walletDto = new WalletDto();
        walletDto.setWalletId(100L);
        walletDto.setWalletAddress("wallet-123");
        walletDto.setWalletBalance(0.0);
        walletDto.setAccountId(1L);

        when(walletClient.getWalletID(any(AccountDto.class))).thenReturn(walletDto);

        ApiResponseDto response = accountService.createAccount(requestDto);

        assertEquals(HttpStatus.CREATED.value(), response.getResponseCode());
        assertEquals("Account creation was successful", response.getResponseMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof WalletDto);
        verify(accountRepository).save(any(Account.class));
        verify(walletClient).getWalletID(any(AccountDto.class));
    }

    @Test
    public void testCreateAccount_Conflict() {
        AccountRequestDto requestDto = new AccountRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPhone("1234567890");

        when(accountRepository.findByEmailOrPhone("test@example.com", "1234567890"))
                .thenReturn(Optional.of(new Account()));

        ApiResponseDto response = accountService.createAccount(requestDto);

        assertEquals(HttpStatus.CONFLICT.value(), response.getResponseCode());
        assertEquals("Account with this phone or email Already exist", response.getResponseMessage());
        assertNull(response.getData());
        verify(accountRepository, never()).save(any(Account.class));
        verify(walletClient, never()).getWalletID(any(AccountDto.class));
    }
}
