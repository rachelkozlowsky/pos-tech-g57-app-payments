package br.com.five.seven.food.infra.clients.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import feign.RequestInterceptor;

@Configuration
public class MercadoPagoClientConfig {

    @Value("${mercadopago.token}")
    private String jwtToken;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + jwtToken);
    }
}
