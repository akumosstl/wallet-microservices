package io.github.akumosstl.transaction.client;

import io.github.akumosstl.commons.dto.WalletDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "wallet-service", url = "${client.wallet-service.url}")
public interface WalletClient {

    @GetMapping("/wallet-api/wallets/{walletId}")
    WalletDto getWalletById(@PathVariable("walletId") Long walletId);

}

