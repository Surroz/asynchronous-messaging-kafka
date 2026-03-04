package org.surro.producerservice.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.surro.producerservice.model.UserOrder;

@Service
public class KafkaProducerService {

    private KafkaTemplate<String, UserOrder> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, UserOrder> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UserOrder message) {
         kafkaTemplate.send("user-order", String.valueOf(message.orderId()), message);
    }
}
