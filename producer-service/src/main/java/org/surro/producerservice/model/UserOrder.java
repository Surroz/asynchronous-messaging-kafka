package org.surro.producerservice.model;

import java.math.BigDecimal;
import java.util.List;

public record UserOrder(int orderId, List<String> dishes, BigDecimal totalPrice) {

}
