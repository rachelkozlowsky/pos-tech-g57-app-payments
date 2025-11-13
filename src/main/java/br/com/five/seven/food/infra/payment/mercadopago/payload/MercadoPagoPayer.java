package br.com.five.seven.food.infra.payment.mercadopago.payload;

import jakarta.validation.constraints.Email;

public record MercadoPagoPayer(
                @Email String email) {

}
