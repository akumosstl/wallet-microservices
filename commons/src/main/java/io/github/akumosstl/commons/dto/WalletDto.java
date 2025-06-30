package io.github.akumosstl.commons.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class WalletDto implements Serializable {

    private Long walletId;

    private String walletAddress;

    private Double walletBalance;

    private Long accountId;
}
