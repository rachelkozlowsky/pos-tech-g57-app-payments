package br.com.five.seven.food.infra.exceptions;

public class PaymentOrderNotFoundException extends RuntimeException {

    public PaymentOrderNotFoundException(String msg){
        super(msg);
    }
}
