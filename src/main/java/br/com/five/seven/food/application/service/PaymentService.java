package br.com.five.seven.food.application.service;

import br.com.five.seven.food.domain.repository.*;
import br.com.five.seven.food.rest.request.NotificationRequest;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;
import br.com.five.seven.food.application.ports.in.PaymentUseCase;
import br.com.five.seven.food.infra.exceptions.PaymentOrderAlreadyApprovedException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class PaymentService
        implements PaymentUseCase, PaymentNotificationUseCase {

    private final IPaymentRepository paymentRepositoryOut;
    private final IPaymentIntegrationRepository paymentIntegrationServiceOut;
    private final IPaymentCheckoutRepository paymentCkeckoutServiceOut;

    public PaymentService(
            IPaymentRepository paymentRepositoryOut,
            IPaymentIntegrationRepository paymentIntegrationServiceOut,
            IPaymentCheckoutRepository paymentCkeckoutServiceOut) {

        this.paymentRepositoryOut = paymentRepositoryOut;
        this.paymentIntegrationServiceOut = paymentIntegrationServiceOut;
        this.paymentCkeckoutServiceOut = paymentCkeckoutServiceOut;
    }

    @Override
    public PaymentOrder findById(String PaymentId) {
        return paymentRepositoryOut.findById(PaymentId);
    }

    @Override
    public PaymentOrder update(PaymentOrder order) {
        return paymentRepositoryOut.update(order);
    }

    @Override
    public List<String> listPaymentOptions() {
        return Stream.of(PaymentOption.values())
                .map(PaymentOption::getDescription)
                .toList();
    }

    @Override
    public void deletePaymentOrderById(String paymentId) {
        paymentRepositoryOut.deleteById(paymentId);
    }

    @Override
    public List<PaymentOrder> findAll(int page, int size) {
        return paymentRepositoryOut.findAll(page, size);
    }

    @Override
    public PaymentOrder createPaymentQRCodePix(String email, String orderId, BigDecimal totalAmount) {

        if (paymentRepositoryOut.isPaymentOrderAprovedByOrderId(orderId)) {
            throw new PaymentOrderAlreadyApprovedException(String.format("Payment with status approved for order %s", orderId));
        }

        if (paymentRepositoryOut.existsByorderId(orderId)) {
            return paymentRepositoryOut.findByorderId(orderId);
        }

        var createPaymentIntegrationQRCodePix = paymentIntegrationServiceOut.createPaymentIntegrationQRCodePix(
                orderId,
                email,
                totalAmount);

        return paymentRepositoryOut.save(createPaymentIntegrationQRCodePix);

    }

    @Override
    public void paymentNotification(NotificationRequest event) {
        if (event.getType().equals("payment") && event.getAction().equals("payment.updated")) {
            var paymentOrderStatus = paymentCkeckoutServiceOut.getPaymentStatus(event.getData().id());

            var paymentOrder = paymentRepositoryOut
                    .findByintegrationId(event.getData().id());
            paymentOrder.setStatus(paymentOrderStatus);
            paymentOrder.setLastUpdate(LocalDateTime.now().toString());

            // var paymentOrderUpdated = paymentRepositoryOut.update(paymentOrder);

            // TODO: Avisar API Principal
        }
    }
}
