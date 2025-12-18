package br.com.five.seven.food.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentOrderNotFoundException extends RuntimeException {

    public PaymentOrderNotFoundException(String msg){
        super(msg);
    }
}
