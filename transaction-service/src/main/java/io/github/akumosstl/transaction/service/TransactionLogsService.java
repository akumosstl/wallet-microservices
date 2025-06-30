package io.github.akumosstl.transaction.service;


import io.github.akumosstl.commons.dto.ApiResponseDto;
import io.github.akumosstl.commons.dto.TransactionResultDto;
import io.github.akumosstl.transaction.model.TransactionLogs;
import io.github.akumosstl.transaction.repository.TransactionLogsRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransactionLogsService {

    @Autowired
    private TransactionLogsService logsService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransactionLogsRepository transactionsLogsRepository;

    public void saveTransactionLogs(TransactionResultDto data) {
        transactionsLogsRepository.save(toEntity(data));
    }

    private TransactionLogs toEntity(TransactionResultDto dto) {
        return modelMapper.map(dto, TransactionLogs.class);
    }

    public List<TransactionLogs> findAll() {
        return transactionsLogsRepository.findAll();
    }
}
