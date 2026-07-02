package org.surro.producerservice.model;

import java.util.List;

public record UserOrder(int orderId, List<String> dishes, double totalPrice) {

}
