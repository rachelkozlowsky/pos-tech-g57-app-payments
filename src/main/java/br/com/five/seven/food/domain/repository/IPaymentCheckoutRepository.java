package br.com.five.seven.food.domain.repository;

import br.com.five.seven.food.domain.enums.PaymentStatus;

public interface IPaymentCheckoutRepository {

    PaymentStatus getPaymentStatus(String id);
}
