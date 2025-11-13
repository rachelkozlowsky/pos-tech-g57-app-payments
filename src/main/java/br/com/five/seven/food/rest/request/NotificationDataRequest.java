package br.com.five.seven.food.rest.request;

import jakarta.validation.constraints.NotNull;

public record NotificationDataRequest(
        @NotNull(message = "Integration id é obrigatório") String id) {

}
