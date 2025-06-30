package io.github.akumosstl.wallet.service;

import io.github.akumosstl.commons.constant.AppConstants;
import io.github.akumosstl.commons.dto.TransactionResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendDetails(TransactionResultDto message) {
        logger.info("Message 'TransactionResultDto' sent -> {}", message);
        this.kafkaTemplate.send(AppConstants.TRANSACTION_TOPIC_NAME, message);
    }

}