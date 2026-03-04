package org.surro.consumerservice.service;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.surro.consumerservice.model.UserOrder;

import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class KafkaService {
    private final List<UserOrder> orders = new ArrayList<>();

    @KafkaListener(topics = "user-order", groupId = "user-order-group")
    public void receiveOrder(UserOrder userOrder) {
        orders.add(userOrder);
    }
}
