package org.anomaly.anomaly_detection.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anomaly.anomaly_detection.dto.TransactionDto;
import org.anomaly.anomaly_detection.entity.Transaction;
import org.anomaly.anomaly_detection.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProcess {

    private final TransactionRepository transactionRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final FraudEventProducer kafkaProducer;
    private static final int VELOCITY_LIMIT = 5;




    public void transactionProcess(TransactionDto dto) {

        String mockid = "user_" + Math.abs(dto.getV1().hashCode() % 1000);
        String redisKey = "velocity:account:" + mockid;

        Long transactionCount = stringRedisTemplate.opsForValue().increment(redisKey);

        if(transactionCount != null && transactionCount ==1) {
            stringRedisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);
        }

        Transaction.TransactionStatus status = Transaction.TransactionStatus.VALID;
        if(transactionCount != null && transactionCount > VELOCITY_LIMIT) {
            log.info("Suspicious activity from the user : {}", mockid);
            status = Transaction.TransactionStatus.SUSPICIOUS;
        }

        Transaction entity = Transaction.builder()
                .transactionTime(dto.getTime())
                .v1(dto.getV1()).v2(dto.getV2()).v3(dto.getV3()).v4(dto.getV4())
                .v5(dto.getV5()).v6(dto.getV6()).v7(dto.getV7()).v8(dto.getV8())
                .v9(dto.getV9()).v10(dto.getV10()).v11(dto.getV11()).v12(dto.getV12())
                .v13(dto.getV13()).v14(dto.getV14()).v15(dto.getV15()).v16(dto.getV16())
                .v17(dto.getV17()).v18(dto.getV18()).v19(dto.getV19()).v20(dto.getV20())
                .v21(dto.getV21()).v22(dto.getV22()).v23(dto.getV23()).v24(dto.getV24())
                .v25(dto.getV25()).v26(dto.getV26()).v27(dto.getV27()).v28(dto.getV28())
                .amount(dto.getAmount())
                .originalClass(dto.getClassLabel())
                .status(status)
                .ingestedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(entity);
        kafkaProducer.publishKafkaTemplate(entity);
    }


}
