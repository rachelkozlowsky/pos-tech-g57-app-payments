package br.com.five.seven.food.infra.clients.payment.payload;

import jakarta.validation.constraints.Email;

public record MercadoPagoPayer(
                @Email String email) {

}
