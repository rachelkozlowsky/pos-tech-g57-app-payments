package br.com.five.seven.food.domain.enums;

public enum PaymentOption {

    PIX("pix");

    private final String description;

    PaymentOption(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
