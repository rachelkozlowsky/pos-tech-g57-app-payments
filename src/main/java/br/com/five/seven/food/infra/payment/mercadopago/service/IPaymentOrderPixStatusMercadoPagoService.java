package br.com.five.seven.food.infra.payment.mercadopago.service;

import br.com.five.seven.food.domain.repository.IPaymentCheckoutRepository;
import br.com.five.seven.food.infra.payment.mercadopago.http.PaymentMercadoPagoClient;
import br.com.five.seven.food.infra.payment.mercadopago.payload.PaymentStatusMercadoPago;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("${ENVIRONMENT_PROFILE_PROD_NAME}")
public class IPaymentOrderPixStatusMercadoPagoService implements IPaymentCheckoutRepository {

    private final PaymentMercadoPagoClient paymentMercadoPagoClient;

    @Override
    public PaymentStatus getPaymentStatus(String id) {
        var mercadoPagoResponse = paymentMercadoPagoClient.getPaymentById(id);
        var statusEnum = PaymentStatusMercadoPago.valueOf(mercadoPagoResponse.status().toUpperCase());
        return statusEnum.getDescription().equals(PaymentStatus.APPROVED.getDescription()) ? PaymentStatus.APPROVED : PaymentStatus.PENDING;
    }

}
