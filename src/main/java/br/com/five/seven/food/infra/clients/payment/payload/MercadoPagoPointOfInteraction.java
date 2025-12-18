package br.com.five.seven.food.infra.clients.payment.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.five.seven.food.rest.response.TransactionData;

public record MercadoPagoPointOfInteraction(
        @JsonProperty("transaction_data") TransactionData transactionData) {

}
