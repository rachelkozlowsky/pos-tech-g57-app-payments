package br.com.five.seven.food.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionData(
    @JsonProperty("qr_code") String qrCode,
    @JsonProperty("qr_code_base64") String qrCodeBase64) {

}
