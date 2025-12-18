package br.com.five.seven.food.application.ports.out;

import java.math.BigDecimal;

public interface OrdersClientOut {
    BigDecimal getAmountByOrderId(String orderId);
}
