package org.surro.consumerservice.service;

import lombok.Getter;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.stereotype.Service;
import org.surro.consumerservice.model.UserOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
public class KafkaService {
    private final List<UserOrder> orders = new ArrayList<>();
    private final List<UserOrder> dltOrders = new ArrayList<>();

    @KafkaListener(topics = "user-order", groupId = "user-order-group")
    @RetryableTopic(attempts = "4",
            backOff = @BackOff(delayString = "1s", maxDelayString = "8s",  multiplier = 2),
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            sameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.MULTIPLE_TOPICS,
            numPartitions = "3",
            exclude = IllegalArgumentException.class
            )
    public void receiveOrder(UserOrder userOrder) {
        if (BigDecimal.ZERO.compareTo(userOrder.totalPrice()) >= 0)
            throw new IllegalArgumentException("UserOrder totalPrice  incorrect vlue: " + userOrder.totalPrice());
        orders.add(userOrder);
    }

    @DltHandler
    public void processDltMessage(UserOrder userOrder) {
        dltOrders.add(userOrder);
    }
}
