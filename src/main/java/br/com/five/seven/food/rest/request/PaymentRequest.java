package br.com.five.seven.food.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(@NotNull(message = "cpf é obrigatório") String cpf,
                              @NotBlank(message = "Order id não pode estar em branco") String orderId) {
}
