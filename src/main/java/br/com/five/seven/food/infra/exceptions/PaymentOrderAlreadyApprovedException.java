package br.com.five.seven.food.infra.exceptions;

public class PaymentOrderAlreadyApprovedException extends RuntimeException {
    
    public PaymentOrderAlreadyApprovedException(String msg){
        super(msg);
    }
}
