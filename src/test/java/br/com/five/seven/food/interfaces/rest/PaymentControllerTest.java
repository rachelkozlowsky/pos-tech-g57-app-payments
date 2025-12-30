package br.com.five.seven.food.interfaces.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.five.seven.food.application.ports.in.PaymentUseCase;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.rest.PaymentController;
import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.rest.request.PaymentRequest;
import br.com.five.seven.food.rest.response.PaymentOrderResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Payment Controller BDD Tests")
class PaymentControllerTest {

    @Mock
    private PaymentUseCase paymentUseCase;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentController paymentController;

    // CREATE PAYMENT TESTS

    @Test
    @DisplayName("Scenario: Create payment QR code PIX successfully")
    void givenValidPaymentRequest_whenCreatePaymentQRCodePix_thenReturnPaymentResponse() {
        // Given
        PaymentRequest request = new PaymentRequest("12345678900", "order123");
        PaymentOrder paymentOrder = createPaymentOrder();
        PaymentOrderResponse response = createPaymentOrderResponse();

        when(paymentUseCase.getAmountByOrderId("order123")).thenReturn(BigDecimal.valueOf(100.0));
        when(paymentUseCase.getEmailByUserCpf("12345678900")).thenReturn("user@email.com");
        when(paymentUseCase.createPaymentQRCodePix("user@email.com", "order123", BigDecimal.valueOf(100.0))).thenReturn(paymentOrder);
        when(paymentMapper.toResponse(paymentOrder)).thenReturn(response);

        // When
        ResponseEntity<PaymentOrderResponse> result = paymentController.createPaymentQRCodePix(request);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(paymentUseCase).getAmountByOrderId("order123");
        verify(paymentUseCase).getEmailByUserCpf("12345678900");
        verify(paymentUseCase).createPaymentQRCodePix("user@email.com", "order123", BigDecimal.valueOf(100.0));
        verify(paymentMapper).toResponse(paymentOrder);
    }

    @Test
    @DisplayName("Scenario: Create payment fails when order not found")
    void givenInvalidOrderId_whenCreatePaymentQRCodePix_thenThrowException() {
        // Given
        PaymentRequest request = new PaymentRequest("12345678900", "invalidOrder");

        when(paymentUseCase.getAmountByOrderId("invalidOrder")).thenThrow(new RuntimeException("Order not found"));

        // When & Then
        assertThrows(RuntimeException.class, () -> paymentController.createPaymentQRCodePix(request));

        verify(paymentUseCase).getAmountByOrderId("invalidOrder");
    }

    // RETRIEVE PAYMENT TESTS

    @Test
    @DisplayName("Scenario: Retrieve payment by ID successfully")
    void givenExistingPaymentId_whenFindById_thenReturnPaymentResponse() {
        // Given
        String paymentId = "payment123";
        PaymentOrder paymentOrder = createPaymentOrder();
        PaymentOrderResponse response = createPaymentOrderResponse();

        when(paymentUseCase.findById(paymentId)).thenReturn(paymentOrder);
        when(paymentMapper.toResponse(paymentOrder)).thenReturn(response);

        // When
        ResponseEntity<PaymentOrderResponse> result = paymentController.findById(paymentId);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(paymentUseCase).findById(paymentId);
        verify(paymentMapper).toResponse(paymentOrder);
    }

    @Test
    @DisplayName("Scenario: Retrieve payment by ID fails when not found")
    void givenNonExistingPaymentId_whenFindById_thenThrowException() {
        // Given
        String paymentId = "nonExisting";

        when(paymentUseCase.findById(paymentId)).thenThrow(new RuntimeException("Payment not found"));

        // When & Then
        assertThrows(RuntimeException.class, () -> paymentController.findById(paymentId));

        verify(paymentUseCase).findById(paymentId);
    }

    @Test
    @DisplayName("Scenario: Retrieve all payments with pagination")
    void givenPageable_whenGetPayments_thenReturnPagedPaymentResponses() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<PaymentOrder> payments = List.of(createPaymentOrder());
        List<PaymentOrderResponse> responses = List.of(createPaymentOrderResponse());

        when(paymentUseCase.findAll(0, 10)).thenReturn(payments);
        when(paymentMapper.toResponse(any(PaymentOrder.class))).thenReturn(responses.get(0));

        // When
        ResponseEntity<Page<PaymentOrderResponse>> result = paymentController.getPayments(pageable);

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().getContent().size());
        verify(paymentUseCase).findAll(0, 10);
        verify(paymentMapper, times(1)).toResponse(any(PaymentOrder.class));
    }

    @Test
    @DisplayName("Scenario: Retrieve payment options")
    void whenListPaymentOptions_thenReturnListOfOptions() {
        // Given
        List<String> options = List.of("PIX", "CREDIT_CARD");

        when(paymentUseCase.listPaymentOptions()).thenReturn(options);

        // When
        ResponseEntity<List<String>> result = paymentController.listPaymentOptions();

        // Then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(options, result.getBody());
        verify(paymentUseCase).listPaymentOptions();
    }

    // DELETE PAYMENT TESTS

    @Test
    @DisplayName("Scenario: Delete payment by ID successfully")
    void givenExistingPaymentId_whenDeletePaymentOrderById_thenReturnNoContent() {
        // Given
        String paymentId = "payment123";

        doNothing().when(paymentUseCase).deletePaymentOrderById(paymentId);

        // When
        ResponseEntity<Void> result = paymentController.deletePaymentOrderById(paymentId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(paymentUseCase).deletePaymentOrderById(paymentId);
    }

    @Test
    @DisplayName("Scenario: Delete payment fails when not found")
    void givenNonExistingPaymentId_whenDeletePaymentOrderById_thenThrowException() {
        // Given
        String paymentId = "nonExisting";

        doThrow(new RuntimeException("Payment not found")).when(paymentUseCase).deletePaymentOrderById(paymentId);

        // When & Then
        assertThrows(RuntimeException.class, () -> paymentController.deletePaymentOrderById(paymentId));

        verify(paymentUseCase).deletePaymentOrderById(paymentId);
    }

    // Helper methods
    private PaymentOrder createPaymentOrder() {
        PaymentOrder order = new PaymentOrder();
        order.setId("payment123");
        order.setOrderId("order123");
        order.setStatus(PaymentStatus.PENDING);
        order.setPaymentOption(PaymentOption.PIX);
        order.setAmount(BigDecimal.valueOf(100.0));
        order.setIntegrationId("int123");
        order.setCorrelationIntegrationId("corr123");
        order.setDateApproved("2023-01-01");
        order.setLastUpdate("2023-01-01");
        order.setQrData("qrdata");
        return order;
    }

    private PaymentOrderResponse createPaymentOrderResponse() {
        return new PaymentOrderResponse(
                "payment123",
                "qrdata",
                "int123",
                "corr123",
                "order123",
                "2023-01-01",
                "2023-01-01",
                PaymentStatus.PENDING,
                PaymentOption.PIX,
                BigDecimal.valueOf(100.0)
        );
    }
}
