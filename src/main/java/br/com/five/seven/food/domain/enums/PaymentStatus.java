package br.com.five.seven.food.domain.enums;

public enum PaymentStatus {

    PENDING("pending"), 
    APPROVED("approved");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
