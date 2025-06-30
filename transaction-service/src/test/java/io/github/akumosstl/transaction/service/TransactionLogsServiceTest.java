package io.github.akumosstl.transaction.service;

import io.github.akumosstl.commons.dto.TransactionResultDto;
import io.github.akumosstl.transaction.model.TransactionLogs;
import io.github.akumosstl.transaction.repository.TransactionLogsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionLogsServiceTest {

    private TransactionLogsService transactionLogsService;

    private TransactionLogsRepository transactionLogsRepository;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        transactionLogsRepository = mock(TransactionLogsRepository.class);
        modelMapper = new ModelMapper();

        transactionLogsService = new TransactionLogsService();
        transactionLogsService = spy(transactionLogsService);

        try {
            var repoField = TransactionLogsService.class.getDeclaredField("transactionsLogsRepository");
            repoField.setAccessible(true);
            repoField.set(transactionLogsService, transactionLogsRepository);

            var mapperField = TransactionLogsService.class.getDeclaredField("modelMapper");
            mapperField.setAccessible(true);
            mapperField.set(transactionLogsService, modelMapper);
        } catch (Exception e) {
            fail("Reflection setup failed: " + e.getMessage());
        }
    }

    @Test
    void testSaveTransactionLogs_shouldSaveEntity() {
        TransactionResultDto dto = new TransactionResultDto();
        dto.setCode(200);
        dto.setType("DEPOSIT");
        dto.setDate(LocalDateTime.now());
        dto.setObject("{\"amount\":100}");

        TransactionLogs entity = modelMapper.map(dto, TransactionLogs.class);
        when(transactionLogsRepository.save(any(TransactionLogs.class))).thenReturn(entity);

        transactionLogsService.saveTransactionLogs(dto);

        verify(transactionLogsRepository, times(1)).save(any(TransactionLogs.class));
    }

    @Test
    void testFindAll_shouldReturnList() {
        List<TransactionLogs> mockList = Collections.singletonList(new TransactionLogs());
        when(transactionLogsRepository.findAll()).thenReturn(mockList);

        List<TransactionLogs> result = transactionLogsService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(transactionLogsRepository, times(1)).findAll();
    }
}
