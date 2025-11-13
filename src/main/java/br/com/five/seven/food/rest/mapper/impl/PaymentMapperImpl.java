package br.com.five.seven.food.rest.mapper.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.rest.response.PaymentOrderResponse;
import org.springframework.stereotype.Component;
import br.com.five.seven.food.infra.payment.mercadopago.payload.MercadoPagoPayer;
import br.com.five.seven.food.infra.payment.mercadopago.payload.PaymentMercadoPagoGetResponse;
import br.com.five.seven.food.infra.payment.mercadopago.payload.PaymentMercadoPagoRequest;
import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentOrderResponse toResponse(PaymentOrder paymentOrder) {
        return new PaymentOrderResponse(
                paymentOrder.getId(),
                paymentOrder.getQrData(),
                paymentOrder.getIntegrationId(),
                paymentOrder.getCorrelationIntegrationId(),
                paymentOrder.getOrderId(),
                paymentOrder.getDateApproved(),
                paymentOrder.getLastUpdate(),
                paymentOrder.getStatus(),
                paymentOrder.getPaymentOption(),
                paymentOrder.getAmount());
    }

    @Override
    public PaymentOrderEntity toEntity(PaymentOrder paymentOrder) {
        var entity = new PaymentOrderEntity();
        entity.setId(paymentOrder.getId());
        entity.setAmount(paymentOrder.getAmount());
        entity.setDateApproved(paymentOrder.getDateApproved());
        entity.setIntegrationId(paymentOrder.getIntegrationId());
        entity.setCorrelationIntegrationId(paymentOrder.getCorrelationIntegrationId());
        entity.setLastUpdate(paymentOrder.getLastUpdate());
        entity.setOrderId(paymentOrder.getOrderId());
        entity.setPaymentOption(paymentOrder.getPaymentOption());
        entity.setQrData(paymentOrder.getQrData());
        entity.setStatus(paymentOrder.getStatus());
        return entity;
    }

    @Override
    public PaymentOrder toDomain(PaymentOrderEntity paymentOrderEntity) {
        var domain = new PaymentOrder();
        domain.setId(paymentOrderEntity.getId());
        domain.setAmount(paymentOrderEntity.getAmount());
        domain.setDateApproved(paymentOrderEntity.getDateApproved());
        domain.setIntegrationId(paymentOrderEntity.getIntegrationId());
        domain.setCorrelationIntegrationId(paymentOrderEntity.getCorrelationIntegrationId());
        domain.setLastUpdate(paymentOrderEntity.getLastUpdate());
        domain.setOrderId(paymentOrderEntity.getOrderId());
        domain.setPaymentOption(paymentOrderEntity.getPaymentOption());
        domain.setQrData(paymentOrderEntity.getQrData());
        domain.setStatus(paymentOrderEntity.getStatus());
        return domain;
    }

    @Override
    public PaymentOrder paymentMercadoPagoGetResponseToDomain(PaymentMercadoPagoGetResponse response, String orderId) {

        var paymentOrder = new PaymentOrder();

        paymentOrder.setOrderId(orderId);
        paymentOrder.setIntegrationId(response.id());
        paymentOrder.setCorrelationIntegrationId(response.externalReference());
        paymentOrder.setStatus(PaymentStatus.PENDING);
        paymentOrder.setDateApproved(response.dateApproved());
        paymentOrder.setQrData(response.pointOfInteraction().transactionData().qrCode());
        paymentOrder.setLastUpdate(LocalDateTime.now().toString());
        paymentOrder.setAmount(response.amount());
        paymentOrder.setPaymentOption(PaymentOption.valueOf(response.paymentMethod().id().toUpperCase()));

        return paymentOrder;

    }

    @Override
    public PaymentOrder paymentMercadoPagoGetResponseToDomain(PaymentMercadoPagoGetResponse response) {

        var paymentOrder = new PaymentOrder();
        paymentOrder.setOrderId(response.externalReference());
        paymentOrder.setIntegrationId(response.id());
        paymentOrder.setCorrelationIntegrationId(response.externalReference());
        paymentOrder.setStatus(PaymentStatus.PENDING);
        paymentOrder.setDateApproved(response.dateApproved());
        paymentOrder.setQrData(response.pointOfInteraction().transactionData().qrCode());
        paymentOrder.setLastUpdate(LocalDateTime.now().toString());
        paymentOrder.setAmount(response.amount());
        paymentOrder.setPaymentOption(PaymentOption.valueOf(response.paymentMethod().id().toUpperCase()));

        return paymentOrder;

    }

    @Override
    public PaymentMercadoPagoRequest toPaymentMercadoPagoPixRequest(BigDecimal amount, String payerEmail,
            String externalReference) {

        var paymentMercadoPagoRequest = new PaymentMercadoPagoRequest(
                new MercadoPagoPayer(payerEmail),
                PaymentOption.PIX.toString().toLowerCase(),
                amount,
                externalReference);

        return paymentMercadoPagoRequest;
    }
}
