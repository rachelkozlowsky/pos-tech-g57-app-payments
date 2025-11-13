package br.com.five.seven.food.infra.payment.mercadopago.payload;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatusMercadoPago {
    PENDING("pending"),
    CANCELLED("cancelled"),
    REJECTED("rejected"),
    APPROVED("approved"),
    IN_PROCESS("in_process"),
    AUTHORIZED("authorized");

    private final String description;

    PaymentStatusMercadoPago(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }
}
