package br.com.five.seven.food.interfaces.rest;

import br.com.five.seven.food.application.ports.in.PaymentNotificationUseCase;
import br.com.five.seven.food.application.ports.in.PaymentUseCase;
import br.com.five.seven.food.config.TestDynamoDBConfig;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.infra.clients.orders.http.OrdersClient;
import br.com.five.seven.food.infra.clients.payment.http.PaymentMercadoPagoClient;
import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.rest.request.PaymentRequest;
import br.com.five.seven.food.rest.response.PaymentOrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestDynamoDBConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentNotificationUseCase paymentOrderNotificationIn;

    @MockitoBean
    private PaymentUseCase paymentOrderServiceIn;

    @MockitoBean
    private PaymentMapper paymentOrderMapper;

    @MockitoBean
    private OrdersClient ordersClient;

    @MockitoBean
    private PaymentMercadoPagoClient paymentMercadoPagoClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findByIdShouldReturnPaymentOrder() throws Exception {
        PaymentOrder paymentOrder = new PaymentOrder();
        PaymentOrderResponse response = new PaymentOrderResponse(
                "payment1",
                "client1",
                "order1",
                "PIX",
                "PENDING",
                "1234567890",
                "1234567890",
                PaymentStatus.APPROVED,
                PaymentOption.PIX,
                BigDecimal.ONE
        );

        when(paymentOrderServiceIn.findById("payment1")).thenReturn(paymentOrder);
        when(paymentOrderMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(get("/v1/payments/payment1"))
                .andExpect(status().isOk());

        verify(paymentOrderServiceIn).findById("payment1");
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void createPaymentQRCodePixShouldReturnPaymentOrder() throws Exception {
        PaymentRequest request = new PaymentRequest("client1", "order1");
        PaymentOrder paymentOrder = new PaymentOrder();
        PaymentOrderResponse response = new PaymentOrderResponse(
                "payment1",
                "client1",
                "order1",
                "PIX",
                "PENDING",
                "1234567890",
                "1234567890",
                PaymentStatus.APPROVED,
                PaymentOption.PIX,
                BigDecimal.ONE
        );

        when(paymentOrderServiceIn.getAmountByOrderId("order1")).thenReturn(BigDecimal.valueOf(100));
        when(paymentOrderServiceIn.getEmailByUserCpf("client1")).thenReturn("client1@email.com");
        when(paymentOrderServiceIn.createPaymentQRCodePix("client1@email.com", "order1", BigDecimal.valueOf(100))).thenReturn(paymentOrder);
        when(paymentOrderMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(post("/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(paymentOrderServiceIn).getAmountByOrderId("order1");
        verify(paymentOrderServiceIn).getEmailByUserCpf("client1");
        verify(paymentOrderServiceIn).createPaymentQRCodePix("client1@email.com", "order1", BigDecimal.valueOf(100));
    }

    @Test
    void getPaymentsShouldReturnPagedPayments() throws Exception {
        List<PaymentOrder> payments = List.of(new PaymentOrder());
        PaymentOrderResponse response = new PaymentOrderResponse(
                "payment1",
                "client1",
                "order1",
                "PIX",
                "PENDING",
                "1234567890",
                "1234567890",
                PaymentStatus.APPROVED,
                PaymentOption.PIX,
                BigDecimal.ONE
        );

        when(paymentOrderServiceIn.findAll(anyInt(), anyInt())).thenReturn(payments);
        when(paymentOrderMapper.toResponse(any())).thenReturn(response);

        mockMvc.perform(get("/v1/payments?page=0&size=10"))
                .andExpect(status().isOk());

        verify(paymentOrderServiceIn).findAll(anyInt(), anyInt());
    }

    @Test
    void listPaymentOptionsShouldReturnOptions() throws Exception {
        List<String> options = List.of("PIX", "CREDIT_CARD");

        when(paymentOrderServiceIn.listPaymentOptions()).thenReturn(options);

        mockMvc.perform(get("/v1/payments/options"))
                .andExpect(status().isOk());

        verify(paymentOrderServiceIn).listPaymentOptions();
    }

    @Test
    @WithMockUser(roles = { "ADMIN" })
    void deletePaymentOrderByIdShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/v1/payments/payment1"))
                .andExpect(status().isNoContent());

        verify(paymentOrderServiceIn).deletePaymentOrderById("payment1");
    }

    @Test
    void createPaymentQRCodePixShouldThrowExceptionWhenOrderNotFound() throws Exception {
        PaymentRequest request = new PaymentRequest("client1", "order1");

        when(paymentOrderServiceIn.getAmountByOrderId("order1")).thenThrow(new RuntimeException("Order not found"));

        mockMvc.perform(post("/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }
}
