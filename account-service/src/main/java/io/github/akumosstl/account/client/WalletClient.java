package io.github.akumosstl.account.client;

import io.github.akumosstl.account.config.FeignClientConfig;
import io.github.akumosstl.commons.dto.AccountDto;
import io.github.akumosstl.commons.dto.WalletDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wallet-service", url = "${client.wallet-service.url}", configuration = FeignClientConfig.class)
public interface WalletClient {

    @PostMapping("/v1/wallet/account")
    WalletDto getWalletID(@RequestBody AccountDto accountDto);

}
