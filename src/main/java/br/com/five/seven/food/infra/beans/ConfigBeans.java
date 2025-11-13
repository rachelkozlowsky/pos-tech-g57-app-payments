package br.com.five.seven.food.infra.beans;

import br.com.five.seven.food.application.service.PaymentService;
import br.com.five.seven.food.domain.repository.*;
import br.com.five.seven.food.infra.payment.mercadopago.http.PaymentMercadoPagoClient;
import br.com.five.seven.food.infra.payment.mercadopago.service.IPaymentOrderPixStatusFakeMercadoPagoService;
import br.com.five.seven.food.infra.payment.mercadopago.service.IPaymentOrderPixStatusMercadoPagoService;
import br.com.five.seven.food.domain.repository.IPaymentRepository;
import br.com.five.seven.food.domain.repository.IPaymentCheckoutRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class ConfigBeans {

    @Bean
    public PaymentService PaymentOrderServiceIn(
            IPaymentRepository IPaymentRepository,
            IPaymentIntegrationRepository IPaymentIntegrationRepository,
            IPaymentCheckoutRepository IPaymentCheckoutRepository) {

        return new PaymentService(
                IPaymentRepository,
                IPaymentIntegrationRepository,
                IPaymentCheckoutRepository);
    }

    @Bean
    @Profile("!prd")
    public IPaymentCheckoutRepository PaymentOrderFakeNotificationAdapter() {
        return new IPaymentOrderPixStatusFakeMercadoPagoService();
    }

    @Bean
    @Profile("prd")
    public IPaymentCheckoutRepository PaymentOrderNotificationAdapter(
            PaymentMercadoPagoClient paymentMercadoPagoClient) {
        return new IPaymentOrderPixStatusMercadoPagoService(paymentMercadoPagoClient);
    }
}
