package br.com.five.seven.food.infra.payment.mercadopago.payload;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentMercadoPagoRequest(
                @NotNull @Valid MercadoPagoPayer payer,
                @JsonProperty("payment_method_id") String paymentMethodId,
                @JsonProperty("transaction_amount") BigDecimal transactionAmount,
                @NotBlank @JsonProperty("external_reference") String externalReference

) {


}
