package io.github.akumosstl.wallet.repository;

import io.github.akumosstl.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByWalletAddress(String walletAddress);

}
