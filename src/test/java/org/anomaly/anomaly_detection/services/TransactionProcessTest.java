package org.anomaly.anomaly_detection.services;

import org.anomaly.anomaly_detection.dto.TransactionDto;
import org.anomaly.anomaly_detection.entity.Transaction;
import org.anomaly.anomaly_detection.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionProcessTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private FraudEventProducer kafkaProducer;


    @InjectMocks
    private TransactionProcess transactionProcess;


    @Test
    void testVelocityLimitTriggersSuspiciousStatus(){
        TransactionDto dto = new TransactionDto();
        dto.setV1(12345.0);

        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.increment(anyString())).thenReturn(6L);


        transactionProcess.transactionProcess(dto);

        ArgumentCaptor<Transaction> captor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(captor.capture());

        Transaction transaction = captor.getValue();

        assertEquals(Transaction.TransactionStatus.SUSPICIOUS, transaction.getStatus(),
                "The transaction should be marked SUSPICIOUS because the velocity limit was exceeded.");

    }
}