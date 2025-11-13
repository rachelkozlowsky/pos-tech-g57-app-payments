package br.com.five.seven.food.rest;

import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.rest.request.NotificationRequest;
import br.com.five.seven.food.rest.response.PaymentOrderResponse;
import br.com.five.seven.food.rest.request.PaymentRequest;
import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;
import br.com.five.seven.food.application.ports.in.PaymentUseCase;
import br.com.five.seven.food.infra.annotations.payment.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Payment", description = "Operations related to payment operations")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payments")
public class PaymentController {

    private final PaymentUseCase paymentOrderServiceIn;
    private final PaymentMapper paymentOrderMapper;


    @SwaggerFindById
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{payment_id}")
    public ResponseEntity<PaymentOrderResponse> findById(@PathVariable(value = "payment_id") String paymentId) {
        var paymentOrder = paymentOrderServiceIn.findById(paymentId);
        return ResponseEntity.ok(paymentOrderMapper.toResponse(paymentOrder));
    }

    @SwaggerCreatePaymentQRCodePix
    @PostMapping
    public ResponseEntity<PaymentOrderResponse> createPaymentQRCodePix(@Valid @RequestBody PaymentRequest paymentRequest) {
        var paymentOrder = paymentOrderServiceIn.createPaymentQRCodePix(paymentRequest.email(), paymentRequest.orderId(), paymentRequest.totalAmount());
        return ResponseEntity.ok(paymentOrderMapper.toResponse(paymentOrder));
    }

    @SwaggerGetPayment
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<PaymentOrderResponse>> getPayments(Pageable pageable) {
        var payments = paymentOrderServiceIn.findAll(pageable.getPageNumber(), pageable.getPageSize());
        var paymentResponses = payments.stream().map(paymentOrderMapper::toResponse)
                .toList();
        return ResponseEntity.ok(new PageImpl<>(paymentResponses, pageable, paymentResponses.size()));
    }

    @SwaggerListPaymentOptions
    @GetMapping("/options")
    public ResponseEntity<List<String>> listPaymentOptions() {
        return ResponseEntity.ok(paymentOrderServiceIn.listPaymentOptions());
    }

    @SwaggerDeletePayment
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{payment_order_id}")
    public ResponseEntity<Void> deletePaymentOrderById(@PathVariable(value = "payment_order_id") String paymentId) {
        paymentOrderServiceIn.deletePaymentOrderById(paymentId);
        return ResponseEntity.noContent().build();
    }

}
