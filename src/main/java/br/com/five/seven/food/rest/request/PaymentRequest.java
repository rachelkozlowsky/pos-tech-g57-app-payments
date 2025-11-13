package br.com.five.seven.food.rest.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(String email,
                             @NotNull(message = "email é obrigatório") @NotBlank(message = "Order id não pode estar em branco") String orderId,  @NotNull(message = "totalAmount é obrigatório") BigDecimal totalAmount) {
}
