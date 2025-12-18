package br.com.five.seven.food.infra.clients.payment.service;

import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.application.ports.out.IPaymentClientOut;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.infra.clients.payment.http.PaymentMercadoPagoClient;
import br.com.five.seven.food.infra.clients.payment.payload.PaymentStatusMercadoPago;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentPixMercadoPagoService implements IPaymentClientOut {

    private final PaymentMercadoPagoClient paymentMercadoPagoClient;
    private final PaymentMapper paymentOrderMapper;

    @Override
    public PaymentOrder createPaymentIntegrationQRCodePix(String orderId, String email, BigDecimal amount) {

        var paymentOrderMercadoPagoPixCreateRequest = paymentOrderMapper.toPaymentMercadoPagoPixRequest(amount,
                email, orderId);

        var responseMercadoPago = paymentMercadoPagoClient
                .createPayment(orderId, paymentOrderMercadoPagoPixCreateRequest);

        return paymentOrderMapper.paymentMercadoPagoGetResponseToDomain(responseMercadoPago,
                orderId);

    }

    @Override
    public PaymentOrder getPaymentOrderIntegrationById(String id) {
        var mercadoPagoResponse = paymentMercadoPagoClient.getPaymentById(id);
        return paymentOrderMapper.paymentMercadoPagoGetResponseToDomain(mercadoPagoResponse);
    }

    

    @Override
    public PaymentStatus getPaymentStatus(String id) {
        var mercadoPagoResponse = paymentMercadoPagoClient.getPaymentById(id);
        var statusEnum = PaymentStatusMercadoPago.valueOf(mercadoPagoResponse.status().toUpperCase());
        return statusEnum.getDescription().equals(PaymentStatus.APPROVED.getDescription()) ? PaymentStatus.APPROVED
                : PaymentStatus.PENDING;
    }
}
