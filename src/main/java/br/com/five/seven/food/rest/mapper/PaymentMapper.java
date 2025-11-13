package br.com.five.seven.food.rest.mapper;

import br.com.five.seven.food.rest.response.PaymentOrderResponse;
import br.com.five.seven.food.infra.payment.mercadopago.payload.PaymentMercadoPagoGetResponse;
import br.com.five.seven.food.infra.payment.mercadopago.payload.PaymentMercadoPagoRequest;
import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.domain.model.PaymentOrder;

import java.math.BigDecimal;

public interface PaymentMapper {


    PaymentOrderResponse toResponse(PaymentOrder order);
    PaymentOrderEntity toEntity(PaymentOrder order);
    PaymentOrder toDomain(PaymentOrderEntity orderEntity);
    PaymentOrder paymentMercadoPagoGetResponseToDomain(PaymentMercadoPagoGetResponse response, String orderId);
    PaymentOrder paymentMercadoPagoGetResponseToDomain(PaymentMercadoPagoGetResponse response);
    PaymentMercadoPagoRequest toPaymentMercadoPagoPixRequest(BigDecimal amount, String payerEmail, String externalReference);
}
