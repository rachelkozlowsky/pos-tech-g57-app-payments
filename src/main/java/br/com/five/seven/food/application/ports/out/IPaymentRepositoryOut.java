package br.com.five.seven.food.application.ports.out;

import br.com.five.seven.food.domain.model.PaymentOrder;

import java.util.List;

public interface IPaymentRepositoryOut {

    List<PaymentOrder> findAll(int page, int size);

    PaymentOrder findById(String id);

    boolean existsById(String id);

    PaymentOrder save(PaymentOrder order);

    boolean isPaymentOrderAprovedByOrderId(String idOrder);

    boolean existsByorderId(String orderId);

    PaymentOrder findByorderId(String orderId);

    PaymentOrder update(PaymentOrder paymentOrderSaved);

    PaymentOrder findByintegrationId(String integrationId);

    void deleteById(String paymentId);

}
