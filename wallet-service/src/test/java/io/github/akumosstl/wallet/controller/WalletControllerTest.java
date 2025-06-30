package io.github.akumosstl.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.commons.dto.AccountDto;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.WalletDto;
import io.github.akumosstl.wallet.dto.FundRequestDto;
import io.github.akumosstl.wallet.dto.TransferRequestDto;
import io.github.akumosstl.wallet.dto.WithdrawRequestDto;
import io.github.akumosstl.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class)
@ContextConfiguration(classes = { WalletController.class, SecurityBypassConfig.class })
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFundWallet() throws Exception {
        FundRequestDto request = new FundRequestDto();
        request.setAmount(100.0);
        request.setWalletAddress("test-wallet");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Funding successful");

        Mockito.when(walletService.fund(any(FundRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/v1/wallet/fund")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Funding successful"));
    }

    @Test
    void testWithdrawWallet() throws Exception {
        WithdrawRequestDto request = new WithdrawRequestDto();
        request.setAmount(50.0);
        request.setWalletAddress("test-wallet");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Withdrawal successful");

        Mockito.when(walletService.withdraw(any(WithdrawRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/v1/wallet/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Withdrawal successful"));
    }

    @Test
    void testTransferWallet() throws Exception {
        TransferRequestDto request = new TransferRequestDto();
        request.setAmount(75.0);
        request.setSourceWalletAddress("wallet-1");
        request.setDestinationWalletAddress("wallet-2");

        ApiResponseDto response = new ApiResponseDto();
        response.setResponseCode(200);
        response.setResponseMessage("Transfer successful");

        Mockito.when(walletService.transfer(any(TransferRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/v1/wallet/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Transfer successful"));
    }

    @Test
    void testCreateWallet() throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountId(123L);

        WalletDto walletDto = new WalletDto();
        walletDto.setWalletId(1L);
        walletDto.setWalletAddress("wallet-address");

        Mockito.when(walletService.create(123L)).thenReturn(walletDto);

        mockMvc.perform(post("/v1/wallet/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletAddress").value("wallet-address"));
    }
}

