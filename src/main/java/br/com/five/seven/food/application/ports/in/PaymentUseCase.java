package br.com.five.seven.food.application.ports.in;

import br.com.five.seven.food.domain.model.PaymentOrder;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentUseCase {
    PaymentOrder findById(String PaymentId);

    PaymentOrder update(PaymentOrder order);

    List<String> listPaymentOptions();

    void deletePaymentOrderById(String paymentId);

    List<PaymentOrder> findAll(int page, int size);

    PaymentOrder createPaymentQRCodePix(String email, String orderId, BigDecimal totalAmount);

    BigDecimal getAmountByOrderId(String orderId);

    String getEmailByUserCpf(String cpf);

}
