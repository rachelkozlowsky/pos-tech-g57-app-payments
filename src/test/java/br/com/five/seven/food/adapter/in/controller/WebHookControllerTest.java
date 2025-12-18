package br.com.five.seven.food.adapter.in.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import br.com.five.seven.food.rest.WebHookController;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.five.seven.food.rest.request.NotificationDataRequest;
import br.com.five.seven.food.rest.request.NotificationRequest;
import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;

public class WebHookControllerTest {
    private final PaymentNotificationUseCase notificationIn = mock(PaymentNotificationUseCase.class);

    private final WebHookController controller = new WebHookController(
            notificationIn);

    @Test
    public void test_paymentNotification_success() {
        NotificationRequest notification = getNotificationRequestMock();
        doNothing().when(notificationIn).paymentNotification(notification);

        ResponseEntity<?> response = controller.paymentNotification(notification);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(notificationIn).paymentNotification(notification);
    }

    private NotificationRequest getNotificationRequestMock() {
        var event = new NotificationRequest();
        var data = new NotificationDataRequest("integration-123");
        event.setAction("payment.updated");
        event.setType("payment");
        event.setData(data);
        return event;
    }
}
