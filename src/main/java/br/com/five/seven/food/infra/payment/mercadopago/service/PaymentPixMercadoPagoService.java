package br.com.five.seven.food.infra.payment.mercadopago.service;

import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.infra.payment.mercadopago.http.PaymentMercadoPagoClient;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.domain.repository.IPaymentIntegrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentPixMercadoPagoService implements IPaymentIntegrationRepository {

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
}
