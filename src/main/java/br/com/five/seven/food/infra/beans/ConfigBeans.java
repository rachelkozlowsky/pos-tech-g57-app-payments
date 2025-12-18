package br.com.five.seven.food.infra.beans;

import br.com.five.seven.food.application.ports.out.IPaymentClientOut;
import br.com.five.seven.food.application.ports.out.IPaymentRepositoryOut;
import br.com.five.seven.food.application.ports.out.OrdersClientOut;
import br.com.five.seven.food.application.ports.out.UsersClientOut;
import br.com.five.seven.food.application.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ConfigBeans {

    @Bean
    public PaymentService PaymentOrderServiceIn(
            IPaymentRepositoryOut IPaymentRepositoryOut,
            IPaymentClientOut IPaymentIntegrationOut, OrdersClientOut ordersClientOut,
            UsersClientOut usersClientOut) {

        return new PaymentService(
                IPaymentRepositoryOut,
                IPaymentIntegrationOut,
                ordersClientOut,
                usersClientOut);
    }
}
