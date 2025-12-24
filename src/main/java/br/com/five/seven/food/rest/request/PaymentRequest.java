package br.com.five.seven.food.rest.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(String cpf,
                              @NotBlank(message = "Order id não pode estar em branco") String orderId) {
}
