package br.com.five.seven.food.domain.model;

import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentOrder {

    private String id;
    private PaymentStatus status;
    private PaymentOption paymentOption;
    private String orderId;
    private String integrationId;
    private String correlationIntegrationId;
    private BigDecimal amount;
    private String dateApproved;
    private String lastUpdate;
    private String qrData;

    public PaymentOrder() {

    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentOption getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(PaymentOption paymentOption) {
        this.paymentOption = paymentOption;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(String dateApproved) {
        this.dateApproved = dateApproved;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }

    public String getCorrelationIntegrationId() {
        return correlationIntegrationId;
    }

    public void setCorrelationIntegrationId(String correlationIntegrationId) {
        this.correlationIntegrationId = correlationIntegrationId;
    }
}
