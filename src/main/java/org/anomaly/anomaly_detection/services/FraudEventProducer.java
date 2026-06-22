package org.anomaly.anomaly_detection.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anomaly.anomaly_detection.dto.TransactionDto;
import org.anomaly.anomaly_detection.entity.Transaction;
import org.hibernate.spi.TreatedNavigablePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FraudEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public FraudEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());

    }

    public void publishKafkaTemplate(Transaction transaction) {
        try{

            String payLoad = objectMapper.writeValueAsString(transaction);
            String kafkaId = transaction.getId().toString();

            if(transaction.getStatus() == Transaction.TransactionStatus.SUSPICIOUS){
                log.info("SUSPICIOUS TRANSACTION STATUS PROCESSING 💀");
                kafkaTemplate.send("transaction-suspicious", kafkaId, payLoad);
            }
            else{
                log.info("Successfully Transaction ✅");
                kafkaTemplate.send("transaction-valid", kafkaId, payLoad);
            }


        }
        catch (JsonProcessingException e){
            log.error("Failed to serialize transaction for Kafka. TxID: {}", transaction.getId(), e);
        }
    }
}
