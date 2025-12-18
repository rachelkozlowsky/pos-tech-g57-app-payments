package br.com.five.seven.food.application.ports.out;

import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;

import java.math.BigDecimal;

public interface IPaymentClientOut {

    PaymentOrder getPaymentOrderIntegrationById(String id);

    PaymentOrder createPaymentIntegrationQRCodePix(String orderId, String email, BigDecimal amount);

    PaymentStatus getPaymentStatus(String id);
}
