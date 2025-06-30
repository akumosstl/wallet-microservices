package io.github.akumosstl.transaction.repository;

import io.github.akumosstl.transaction.model.TransactionLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, Long> {
}
