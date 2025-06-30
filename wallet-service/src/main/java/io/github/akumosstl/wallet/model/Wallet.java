package io.github.akumosstl.wallet.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "wallet", schema = "wallet")
public class Wallet implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "wallet_balance")
    private Double walletBalance;

    @Column(name="date_created")
    private LocalDateTime dateCreated;

    @Column(name = "account_id")
    private Long accountId;

}
