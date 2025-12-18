package br.com.five.seven.food.infra.clients.payment.http;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.five.seven.food.infra.clients.payment.config.MercadoPagoClientConfig;
import br.com.five.seven.food.infra.clients.payment.payload.PaymentMercadoPagoFetchResponse;
import br.com.five.seven.food.infra.clients.payment.payload.PaymentMercadoPagoGetResponse;
import br.com.five.seven.food.infra.clients.payment.payload.PaymentMercadoPagoRequest;


@FeignClient(name = "PaymentOrderPixMercadoPagoRepository", url = "${spring.application.payment-integration.host1}", configuration = MercadoPagoClientConfig.class)
public interface PaymentMercadoPagoClient {

    @PostMapping("/v1/payments")
    PaymentMercadoPagoGetResponse createPayment(@RequestHeader("X-Idempotency-Key") String xIdempotencyKey,
                                                @RequestBody PaymentMercadoPagoRequest order);

    @GetMapping("/v1/payments/search")
    PaymentMercadoPagoFetchResponse getPaymentList();

    @GetMapping("/v1/payments/{id}")
    PaymentMercadoPagoGetResponse getPaymentById(@PathVariable("id") String idPayment);

}
