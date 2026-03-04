package org.surro.consumerservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.surro.consumerservice.model.UserOrder;
import org.surro.consumerservice.service.KafkaService;

import java.util.List;

@RestController
@RequestMapping("/kafka")
public class KafkaController {
    private final KafkaService kafkaService;

    public KafkaController(KafkaService kafkaService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/get-orders")
    public ResponseEntity<List<UserOrder>> getOrders() {
       return ResponseEntity.ok(kafkaService.getOrders());
    }
}
