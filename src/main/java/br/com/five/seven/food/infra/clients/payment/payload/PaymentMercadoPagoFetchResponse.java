package br.com.five.seven.food.infra.clients.payment.payload;

import java.util.List;

public record PaymentMercadoPagoFetchResponse(
                List<PaymentMercadoPagoGetResponse> results) {

}
