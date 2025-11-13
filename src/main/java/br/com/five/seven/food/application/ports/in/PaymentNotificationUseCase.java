package br.com.five.seven.food.application.ports.in;

import br.com.five.seven.food.rest.request.NotificationRequest;

public interface PaymentNotificationUseCase {

    void paymentNotification(NotificationRequest event);
}
