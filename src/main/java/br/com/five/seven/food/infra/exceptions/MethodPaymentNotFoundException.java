package br.com.five.seven.food.infra.exceptions;

public class MethodPaymentNotFoundException extends RuntimeException {

    public MethodPaymentNotFoundException(String msg) {
        super(msg);
    }
}
