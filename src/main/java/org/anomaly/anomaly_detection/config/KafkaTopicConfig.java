package org.anomaly.anomaly_detection.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic validTransaction() {
        return TopicBuilder.name("transaction-valid")
                .partitions(3)
                .replicas(1)
                .build();

    }

    @Bean
    public NewTopic suspiciousTransaction() {
        return TopicBuilder.name("transaction-suspicious")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
