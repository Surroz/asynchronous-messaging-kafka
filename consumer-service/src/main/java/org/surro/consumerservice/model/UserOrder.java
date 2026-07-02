package org.surro.consumerservice.model;

import java.util.List;

public record UserOrder(int orderId, List<String> dishes, double totalPrice) {

}
