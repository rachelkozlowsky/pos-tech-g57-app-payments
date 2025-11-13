package br.com.five.seven.food.domain.repository;

import br.com.five.seven.food.domain.model.PaymentOrder;

import java.math.BigDecimal;

public interface IPaymentIntegrationRepository {

    PaymentOrder getPaymentOrderIntegrationById(String id);

    PaymentOrder createPaymentIntegrationQRCodePix(String orderId, String email, BigDecimal amount);
}
