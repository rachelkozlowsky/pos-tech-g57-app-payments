package br.com.five.seven.food.rest;

import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;
import br.com.five.seven.food.infra.annotations.payment.SwaggerPaymentNotification;
import br.com.five.seven.food.rest.request.NotificationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Web Hook Payment", description = "Operations related to Web Hook payment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/webhook")
public class WebHookController {

    private final PaymentNotificationUseCase paymentOrderNotificationIn;

    @SwaggerPaymentNotification
    @PostMapping("/payment")
    public ResponseEntity<Void> paymentNotification(@Valid @RequestBody NotificationRequest event) {
        paymentOrderNotificationIn.paymentNotification(event);
        return ResponseEntity.ok().build();
    }
}
