package br.com.five.seven.food.infra.payment.mercadopago.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.Logger;
import feign.RequestInterceptor;

@Configuration
public class MercadoPagoClientConfig {

    @Value("${mercadopago.token}")
    private String jwtToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + jwtToken);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
