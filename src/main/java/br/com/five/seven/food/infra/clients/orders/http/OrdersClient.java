package br.com.five.seven.food.infra.clients.orders.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.five.seven.food.infra.clients.orders.config.OrdersClientConfig;
import br.com.five.seven.food.infra.clients.orders.payload.OrderResponse;


@FeignClient(name = "OrdersService", url = "${spring.application.orders-client.host1}", configuration = OrdersClientConfig.class)
public interface OrdersClient {

    @GetMapping("/v1/orders/{id}")
    ResponseEntity<OrderResponse> getAmountByOrderId(@PathVariable("id") String idOrder);

}
