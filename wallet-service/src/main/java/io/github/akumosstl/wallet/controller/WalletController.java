package io.github.akumosstl.wallet.controller;


import io.github.akumosstl.commons.dto.AccountDto;
import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.WalletDto;
import io.github.akumosstl.wallet.dto.FundRequestDto;
import io.github.akumosstl.wallet.dto.TransferRequestDto;
import io.github.akumosstl.wallet.dto.WithdrawRequestDto;
import io.github.akumosstl.wallet.service.WalletService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/wallet")
public class WalletController {

    @Autowired
    WalletService walletService;

    @PostMapping("/fund")
    @ApiOperation(value = "Fund Wallet",notes = "Add funds to wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> fund(@Valid @RequestBody FundRequestDto data) {
        return ResponseEntity.ok().body(walletService.fund(data));
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "Withdraw Funds",notes = "Withdraw funds from wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> withdraw(@Valid @RequestBody WithdrawRequestDto data){
        return ResponseEntity.ok().body(walletService.withdraw(data));
    }

    @PostMapping("/transfer")
    @ApiOperation(value = "Withdraw Funds",notes = "Withdraw funds from wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> transfer(@Valid @RequestBody TransferRequestDto data){
        return ResponseEntity.ok().body(walletService.transfer(data));
    }

    @PostMapping("/account")
    @ApiOperation(value = "Create Wallet given an account id",notes = "Creating a wallet given an account id")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<WalletDto> create(@RequestBody AccountDto accountDto){
        return ResponseEntity.ok().body(walletService.create(accountDto.getAccountId()));
    }

}