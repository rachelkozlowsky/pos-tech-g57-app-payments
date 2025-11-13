package br.com.five.seven.food.rest.response;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PaymentOrderResponse(
                @NotEmpty String id,
                @JsonProperty("qr_data") @NotEmpty String qrData,
                @JsonProperty("integration_id") @NotEmpty String integrationId,
                @JsonProperty("correlation_integration_id") @NotEmpty String correlationIntegrationId,
                @JsonProperty("order_id") @NotEmpty String orderId,
                @JsonProperty("date_approved") @NotEmpty String dateApproved,
                @JsonProperty("last_update") @NotEmpty String lastUpdate,
                @NotNull PaymentStatus status,
                @JsonProperty("payment_option") @NotNull PaymentOption paymentOption,
                @NotNull BigDecimal amount) {

}
