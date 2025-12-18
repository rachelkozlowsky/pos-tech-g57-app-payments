package br.com.five.seven.food.application.service;

import br.com.five.seven.food.application.ports.out.IPaymentClientOut;
import br.com.five.seven.food.application.ports.out.IPaymentRepositoryOut;
import br.com.five.seven.food.application.ports.out.OrdersClientOut;
import br.com.five.seven.food.application.ports.out.UsersClientOut;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.infra.exceptions.PaymentOrderAlreadyApprovedException;
import br.com.five.seven.food.infra.exceptions.PaymentOrderNotFoundException;
import br.com.five.seven.food.rest.request.NotificationRequest;
import br.com.five.seven.food.rest.request.NotificationDataRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private IPaymentRepositoryOut paymentRepositoryOut;

    @Mock
    private IPaymentClientOut paymentClientOut;

    @Mock
    private OrdersClientOut ordersClientOut;

    @Mock
    private UsersClientOut usersClientOut;

    @InjectMocks
    private PaymentService paymentService;

    private PaymentOrder paymentOrder;

    @BeforeEach
    void setUp() {
        paymentOrder = new PaymentOrder();
        paymentOrder.setId("payment-123");
        paymentOrder.setOrderId("order-123");
        paymentOrder.setStatus(PaymentStatus.PENDING);
        paymentOrder.setPaymentOption(PaymentOption.PIX);
        paymentOrder.setAmount(BigDecimal.valueOf(100.00));
    }

    @Test
    void findById_ShouldReturnPaymentOrder_WhenExists() {
        when(paymentRepositoryOut.findByorderId("order-123")).thenReturn(paymentOrder);

        PaymentOrder result = paymentService.findById("order-123");

        assertNotNull(result);
        assertEquals("order-123", result.getOrderId());
        verify(paymentRepositoryOut).findByorderId("order-123");
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        when(paymentRepositoryOut.findByorderId("non-existent")).thenReturn(null);

        assertThrows(PaymentOrderNotFoundException.class, () -> {
            paymentService.findById("non-existent");
        });

        verify(paymentRepositoryOut).findByorderId("non-existent");
    }

    @Test
    void update_ShouldReturnUpdatedPaymentOrder() {
        when(paymentRepositoryOut.update(any(PaymentOrder.class))).thenReturn(paymentOrder);

        PaymentOrder result = paymentService.update(paymentOrder);

        assertNotNull(result);
        assertEquals(paymentOrder, result);
        verify(paymentRepositoryOut).update(paymentOrder);
    }

    @Test
    void listPaymentOptions_ShouldReturnAllOptions() {
        List<String> result = paymentService.listPaymentOptions();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains("pix"));
    }

    @Test
    void deletePaymentOrderById_ShouldCallRepository() {
        doNothing().when(paymentRepositoryOut).deleteById("payment-123");

        paymentService.deletePaymentOrderById("payment-123");

        verify(paymentRepositoryOut).deleteById("payment-123");
    }

    @Test
    void findAll_ShouldReturnPagedPayments() {
        List<PaymentOrder> payments = List.of(paymentOrder);
        when(paymentRepositoryOut.findAll(0, 10)).thenReturn(payments);

        List<PaymentOrder> result = paymentService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(paymentRepositoryOut).findAll(0, 10);
    }

    @Test
    void createPaymentQRCodePix_ShouldThrowException_WhenPaymentAlreadyApproved() {
        when(paymentRepositoryOut.isPaymentOrderAprovedByOrderId("order-123")).thenReturn(true);

        assertThrows(PaymentOrderAlreadyApprovedException.class, () -> {
            paymentService.createPaymentQRCodePix("test@test.com", "order-123", BigDecimal.valueOf(100.00));
        });

        verify(paymentRepositoryOut).isPaymentOrderAprovedByOrderId("order-123");
        verify(paymentRepositoryOut, never()).save(any());
    }

    @Test
    void createPaymentQRCodePix_ShouldReturnExisting_WhenPaymentExists() {
        when(paymentRepositoryOut.isPaymentOrderAprovedByOrderId("order-123")).thenReturn(false);
        when(paymentRepositoryOut.existsByorderId("order-123")).thenReturn(true);
        when(paymentRepositoryOut.findByorderId("order-123")).thenReturn(paymentOrder);

        PaymentOrder result = paymentService.createPaymentQRCodePix("test@test.com", "order-123", BigDecimal.valueOf(100.00));

        assertNotNull(result);
        assertEquals(paymentOrder, result);
        verify(paymentRepositoryOut).existsByorderId("order-123");
        verify(paymentRepositoryOut).findByorderId("order-123");
        verify(paymentClientOut, never()).createPaymentIntegrationQRCodePix(anyString(), anyString(), any());
    }

    @Test
    void createPaymentQRCodePix_ShouldCreateNew_WhenPaymentDoesNotExist() {
        PaymentOrder newPayment = new PaymentOrder();
        newPayment.setOrderId("order-456");
        
        when(paymentRepositoryOut.isPaymentOrderAprovedByOrderId("order-456")).thenReturn(false);
        when(paymentRepositoryOut.existsByorderId("order-456")).thenReturn(false);
        when(paymentClientOut.createPaymentIntegrationQRCodePix(anyString(), anyString(), any()))
                .thenReturn(newPayment);
        when(paymentRepositoryOut.save(any(PaymentOrder.class))).thenReturn(newPayment);

        PaymentOrder result = paymentService.createPaymentQRCodePix("test@test.com", "order-456", BigDecimal.valueOf(100.00));

        assertNotNull(result);
        assertEquals("order-456", result.getOrderId());
        verify(paymentClientOut).createPaymentIntegrationQRCodePix("order-456", "test@test.com", BigDecimal.valueOf(100.00));
        verify(paymentRepositoryOut).save(newPayment);
    }

    @Test
    void paymentNotification_ShouldUpdatePayment_WhenEventIsPaymentUpdated() {
        NotificationDataRequest data = new NotificationDataRequest("integration-123");
        NotificationRequest event = new NotificationRequest();
        event.setType("payment");
        event.setAction("payment.updated");
        event.setData(data);
        
        when(paymentClientOut.getPaymentStatus("integration-123")).thenReturn(PaymentStatus.APPROVED);
        when(paymentRepositoryOut.findByintegrationId("integration-123")).thenReturn(paymentOrder);
        when(paymentRepositoryOut.update(any(PaymentOrder.class))).thenReturn(paymentOrder);

        paymentService.paymentNotification(event);

        verify(paymentClientOut).getPaymentStatus("integration-123");
        verify(paymentRepositoryOut).findByintegrationId("integration-123");
        verify(paymentRepositoryOut).update(argThat(po -> 
            po.getStatus() == PaymentStatus.APPROVED && po.getLastUpdate() != null
        ));
    }

    @Test
    void paymentNotification_ShouldNotUpdate_WhenEventTypeIsNotPayment() {
        NotificationDataRequest data = new NotificationDataRequest("integration-123");
        NotificationRequest event = new NotificationRequest();
        event.setType("order");
        event.setAction("payment.updated");
        event.setData(data);

        paymentService.paymentNotification(event);

        verify(paymentClientOut, never()).getPaymentStatus(anyString());
        verify(paymentRepositoryOut, never()).update(any());
    }

    @Test
    void paymentNotification_ShouldNotUpdate_WhenActionIsNotPaymentUpdated() {
        NotificationDataRequest data = new NotificationDataRequest("integration-123");
        NotificationRequest event = new NotificationRequest();
        event.setType("payment");
        event.setAction("payment.created");
        event.setData(data);

        paymentService.paymentNotification(event);

        verify(paymentClientOut, never()).getPaymentStatus(anyString());
        verify(paymentRepositoryOut, never()).update(any());
    }

    @Test
    void getAmountByOrderId_ShouldReturnAmount() {
        when(ordersClientOut.getAmountByOrderId("order-123")).thenReturn(BigDecimal.valueOf(100.00));

        BigDecimal result = paymentService.getAmountByOrderId("order-123");

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(100.00), result);
        verify(ordersClientOut).getAmountByOrderId("order-123");
    }

    @Test
    void getEmailByUserCpf_ShouldReturnEmail_WhenUserExists() {
        var userResponse = new br.com.five.seven.food.infra.clients.users.payload.UsersResponse(
                "user-001",
                "12345678900",
                "João Silva",
                "joao.silva@email.com",
                "(11) 98765-4321"
        );
        
        when(usersClientOut.getUserByCpf("12345678900")).thenReturn(userResponse);

        String email = paymentService.getEmailByUserCpf("12345678900");

        assertNotNull(email);
        assertEquals("joao.silva@email.com", email);
        verify(usersClientOut).getUserByCpf("12345678900");
    }

    @Test
    void getEmailByUserCpf_ShouldReturnDefaultEmail_WhenUserNotFound() {
        when(usersClientOut.getUserByCpf("00000000000")).thenReturn(null);

        String email = paymentService.getEmailByUserCpf("00000000000");

        assertNotNull(email);
        assertEquals("postechg57@gmail.com", email);
        verify(usersClientOut).getUserByCpf("00000000000");
    }
}
