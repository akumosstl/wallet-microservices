package io.github.akumosstl.transaction.service;

import io.github.akumosstl.commons.dto.TransactionResultDto;
import io.github.akumosstl.commons.constant.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafKaConsumerService {

    @Autowired
    private TransactionLogsService logsService;

    private final Logger logger = LoggerFactory.getLogger(KafKaConsumerService.class);

    @KafkaListener(topics = AppConstants.TRANSACTION_TOPIC_NAME, groupId = AppConstants.TRANSACTION_GROUP_ID)
    public void consume(TransactionResultDto message) {
        logger.info("Transaction Message received -> {}", message);
        logsService.saveTransactionLogs(message);

    }

}