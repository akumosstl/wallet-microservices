package io.github.akumosstl.wallet.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WalletDto implements Serializable {

    private Long walletId;

    private String walletAddress;

    private Double walletBalance;

    private Long accountId;
}
