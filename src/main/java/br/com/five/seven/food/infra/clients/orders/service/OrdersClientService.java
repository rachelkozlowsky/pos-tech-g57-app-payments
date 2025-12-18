package br.com.five.seven.food.infra.clients.orders.service;

import br.com.five.seven.food.application.ports.out.OrdersClientOut;
import br.com.five.seven.food.infra.clients.orders.http.OrdersClient;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrdersClientService implements OrdersClientOut {

    private final OrdersClient ordersClient;

    @Override
    public BigDecimal getAmountByOrderId(String orderId) {
        var response = ordersClient.getAmountByOrderId(orderId);
        if (response.getBody() == null) {
            throw new IllegalStateException("Order not found");
        }

        return response.getBody().getTotalAmount();
    }
}
