package br.com.five.seven.food.infra.clients.payment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentMethod(
        String id,
        @JsonProperty("issuer_id") String issuerId,
        String type) {

}
