package org.surro.producerservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surro.producerservice.model.UserOrder;
import org.surro.producerservice.service.KafkaProducerService;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaProducerService kafkaProducerService;

    public KafkaController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/publish")
    public void postOrder(@RequestBody UserOrder userOrder) {
        kafkaProducerService.sendMessage(userOrder);
    }
}
