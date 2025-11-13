package br.com.five.seven.food.infra.payment.mercadopago.payload;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentMercadoPagoGetResponse(String id,
                @JsonProperty("point_of_interaction") MercadoPagoPointOfInteraction pointOfInteraction,
                @JsonProperty("date_created") String dateCreated,
                @JsonProperty("payment_method") PaymentMethod paymentMethod,
                @JsonProperty("transaction_amount") BigDecimal amount,
                @JsonProperty("date_approved") String dateApproved,
                @JsonProperty("status_detail") String statusDetail,
                @JsonProperty("external_reference") String externalReference,
                String status,
                MercadoPagoPayer payer) {

}
