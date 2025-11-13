package br.com.five.seven.food.infra.payment.mercadopago.payload;

import java.util.List;

public record PaymentMercadoPagoFetchResponse(
                List<PaymentMercadoPagoGetResponse> results) {

}
