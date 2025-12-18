package br.com.five.seven.food.rest.mapper.impl;

import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.infra.clients.payment.payload.*;
import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.rest.response.PaymentOrderResponse;
import br.com.five.seven.food.rest.response.TransactionData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperImplTest {

    private PaymentMapperImpl mapper;
    private PaymentOrder paymentOrder;
    private PaymentOrderEntity paymentOrderEntity;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapperImpl();

        paymentOrder = new PaymentOrder();
        paymentOrder.setId("payment-123");
        paymentOrder.setOrderId("order-123");
        paymentOrder.setIntegrationId("integration-123");
        paymentOrder.setCorrelationIntegrationId("correlation-123");
        paymentOrder.setStatus(PaymentStatus.PENDING);
        paymentOrder.setPaymentOption(PaymentOption.PIX);
        paymentOrder.setAmount(BigDecimal.valueOf(100.00));
        paymentOrder.setQrData("qr-code-data");
        paymentOrder.setDateApproved("2025-12-10");
        paymentOrder.setLastUpdate("2025-12-10T10:00:00");

        paymentOrderEntity = new PaymentOrderEntity();
        paymentOrderEntity.setId("payment-123");
        paymentOrderEntity.setOrderId("order-123");
        paymentOrderEntity.setIntegrationId("integration-123");
        paymentOrderEntity.setCorrelationIntegrationId("correlation-123");
        paymentOrderEntity.setStatus(PaymentStatus.PENDING);
        paymentOrderEntity.setPaymentOption(PaymentOption.PIX);
        paymentOrderEntity.setAmount(BigDecimal.valueOf(100.00));
        paymentOrderEntity.setQrData("qr-code-data");
        paymentOrderEntity.setDateApproved("2025-12-10");
        paymentOrderEntity.setLastUpdate("2025-12-10T10:00:00");
    }

    @Test
    void toResponse_ShouldConvertPaymentOrderToResponse() {
        PaymentOrderResponse response = mapper.toResponse(paymentOrder);

        assertNotNull(response);
        assertEquals("payment-123", response.id());
        assertEquals("qr-code-data", response.qrData());
        assertEquals("integration-123", response.integrationId());
        assertEquals("correlation-123", response.correlationIntegrationId());
        assertEquals("order-123", response.orderId());
        assertEquals("2025-12-10", response.dateApproved());
        assertEquals("2025-12-10T10:00:00", response.lastUpdate());
        assertEquals(PaymentStatus.PENDING, response.status());
        assertEquals(PaymentOption.PIX, response.paymentOption());
        assertEquals(BigDecimal.valueOf(100.00), response.amount());
    }

    @Test
    void toEntity_ShouldConvertPaymentOrderToEntity() {
        PaymentOrderEntity entity = mapper.toEntity(paymentOrder);

        assertNotNull(entity);
        assertEquals("payment-123", entity.getId());
        assertEquals("order-123", entity.getOrderId());
        assertEquals("integration-123", entity.getIntegrationId());
        assertEquals("correlation-123", entity.getCorrelationIntegrationId());
        assertEquals(PaymentStatus.PENDING, entity.getStatus());
        assertEquals(PaymentOption.PIX, entity.getPaymentOption());
        assertEquals(BigDecimal.valueOf(100.00), entity.getAmount());
        assertEquals("qr-code-data", entity.getQrData());
        assertEquals("2025-12-10", entity.getDateApproved());
        assertEquals("2025-12-10T10:00:00", entity.getLastUpdate());
    }

    @Test
    void toDomain_ShouldConvertEntityToPaymentOrder() {
        PaymentOrder domain = mapper.toDomain(paymentOrderEntity);

        assertNotNull(domain);
        assertEquals("payment-123", domain.getId());
        assertEquals("order-123", domain.getOrderId());
        assertEquals("integration-123", domain.getIntegrationId());
        assertEquals("correlation-123", domain.getCorrelationIntegrationId());
        assertEquals(PaymentStatus.PENDING, domain.getStatus());
        assertEquals(PaymentOption.PIX, domain.getPaymentOption());
        assertEquals(BigDecimal.valueOf(100.00), domain.getAmount());
        assertEquals("qr-code-data", domain.getQrData());
        assertEquals("2025-12-10", domain.getDateApproved());
        assertEquals("2025-12-10T10:00:00", domain.getLastUpdate());
    }

    @Test
    void paymentMercadoPagoGetResponseToDomain_WithOrderId_ShouldConvert() {
        TransactionData transactionData = new TransactionData("qr-code", "qr-base64");
        MercadoPagoPointOfInteraction pointOfInteraction = new MercadoPagoPointOfInteraction(transactionData);
        PaymentMethod paymentMethod = new PaymentMethod("pix", "bank_transfer", null);
        
        PaymentMercadoPagoGetResponse response = new PaymentMercadoPagoGetResponse(
                "mp-123",
                pointOfInteraction,
                "2025-12-10T10:00:00",
                paymentMethod,
                BigDecimal.valueOf(100.00),
                "2025-12-10T10:05:00",
                "accredited",
                "external-ref",
                "approved",
                new MercadoPagoPayer("test@test.com")
        );

        PaymentOrder result = mapper.paymentMercadoPagoGetResponseToDomain(response, "order-456");

        assertNotNull(result);
        assertEquals("order-456", result.getOrderId());
        assertEquals("mp-123", result.getIntegrationId());
        assertEquals("external-ref", result.getCorrelationIntegrationId());
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals("2025-12-10T10:05:00", result.getDateApproved());
        assertEquals("qr-code", result.getQrData());
        assertEquals(BigDecimal.valueOf(100.00), result.getAmount());
        assertEquals(PaymentOption.PIX, result.getPaymentOption());
        assertNotNull(result.getLastUpdate());
    }

    @Test
    void paymentMercadoPagoGetResponseToDomain_WithoutOrderId_ShouldUseExternalReference() {
        TransactionData transactionData = new TransactionData("qr-code", "qr-base64");
        MercadoPagoPointOfInteraction pointOfInteraction = new MercadoPagoPointOfInteraction(transactionData);
        PaymentMethod paymentMethod = new PaymentMethod("pix", "bank_transfer", null);
        
        PaymentMercadoPagoGetResponse response = new PaymentMercadoPagoGetResponse(
                "mp-789",
                pointOfInteraction,
                "2025-12-10T11:00:00",
                paymentMethod,
                BigDecimal.valueOf(200.00),
                null,
                "pending",
                "order-789",
                "pending",
                new MercadoPagoPayer("user@test.com")
        );

        PaymentOrder result = mapper.paymentMercadoPagoGetResponseToDomain(response);

        assertNotNull(result);
        assertEquals("order-789", result.getOrderId());
        assertEquals("mp-789", result.getIntegrationId());
        assertEquals("order-789", result.getCorrelationIntegrationId());
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertNull(result.getDateApproved());
        assertEquals("qr-code", result.getQrData());
        assertEquals(BigDecimal.valueOf(200.00), result.getAmount());
        assertEquals(PaymentOption.PIX, result.getPaymentOption());
        assertNotNull(result.getLastUpdate());
    }

    @Test
    void toPaymentMercadoPagoPixRequest_ShouldCreateRequest() {
        PaymentMercadoPagoRequest request = mapper.toPaymentMercadoPagoPixRequest(
                BigDecimal.valueOf(150.00),
                "payer@test.com",
                "ref-123"
        );

        assertNotNull(request);
        assertEquals(BigDecimal.valueOf(150.00), request.transactionAmount());
        assertEquals("payer@test.com", request.payer().email());
        assertEquals("ref-123", request.externalReference());
        assertEquals("pix", request.paymentMethodId());
    }
}
