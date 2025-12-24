package br.com.five.seven.food.application.service;

import br.com.five.seven.food.rest.request.NotificationRequest;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;
import br.com.five.seven.food.application.ports.in.PaymentUseCase;
import br.com.five.seven.food.application.ports.out.IPaymentClientOut;
import br.com.five.seven.food.application.ports.out.IPaymentRepositoryOut;
import br.com.five.seven.food.application.ports.out.OrdersClientOut;
import br.com.five.seven.food.application.ports.out.UsersClientOut;
import br.com.five.seven.food.infra.exceptions.PaymentOrderAlreadyApprovedException;
import br.com.five.seven.food.infra.exceptions.PaymentOrderNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class PaymentService
        implements PaymentUseCase, PaymentNotificationUseCase {

    private final IPaymentRepositoryOut paymentRepositoryOut;
    private final IPaymentClientOut paymentIntegrationServiceOut;
    private final OrdersClientOut ordersClientOut;
    private final UsersClientOut usersClientOut;

    private final static String DEFAULT_EMAIL = "postechg57@gmail.com";

    public PaymentService(
            IPaymentRepositoryOut paymentRepositoryOut,
            IPaymentClientOut paymentIntegrationServiceOut,
            OrdersClientOut ordersClientOut,
            UsersClientOut usersClientOut) {
        this.paymentRepositoryOut = paymentRepositoryOut;
        this.paymentIntegrationServiceOut = paymentIntegrationServiceOut;
        this.ordersClientOut = ordersClientOut;
        this.usersClientOut = usersClientOut;
    }

    @Override
    public PaymentOrder findById(String PaymentId) {
        PaymentOrder paymentOrder = paymentRepositoryOut.findByorderId(PaymentId);
        if (paymentOrder == null) {
            throw new PaymentOrderNotFoundException(String.format("Payment not found with order id: %s", PaymentId));
        }
        return paymentOrder;
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
            throw new PaymentOrderAlreadyApprovedException(
                    String.format("Payment with status approved for order %s", orderId));
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
            var paymentOrderStatus = paymentIntegrationServiceOut.getPaymentStatus(event.getData().id());

            var paymentOrder = paymentRepositoryOut
                    .findByintegrationId(event.getData().id());
            paymentOrder.setStatus(paymentOrderStatus);
            paymentOrder.setLastUpdate(LocalDateTime.now().toString());

            paymentRepositoryOut.update(paymentOrder);
        }
    }

    @Override
    public BigDecimal getAmountByOrderId(String orderId) {
        return ordersClientOut.getAmountByOrderId(orderId);
    }
    
    @Override
    public String getEmailByUserCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return PaymentService.DEFAULT_EMAIL;
        }
        var userResponse = usersClientOut.getUserByCpf(cpf);

        if (userResponse == null) {
            throw new IllegalArgumentException("Usuário não encontrado para o CPF: " + cpf);
        }

        return userResponse.getEmail();
    }
}
