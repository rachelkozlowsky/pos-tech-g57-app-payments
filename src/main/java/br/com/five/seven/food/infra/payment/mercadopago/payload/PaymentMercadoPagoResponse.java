package br.com.five.seven.food.infra.payment.mercadopago.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentMercadoPagoResponse(
        String id,
        String status,
        @JsonProperty("date_created") String dateCreated,
        @JsonProperty("transaction_amount") String transactionAmount,
        @JsonProperty("date_approved") String dateApproved,
        @JsonProperty("external_reference") String externalReference,
        @JsonProperty("point_of_interaction") MercadoPagoPointOfInteraction pointOfInteraction) {

}
