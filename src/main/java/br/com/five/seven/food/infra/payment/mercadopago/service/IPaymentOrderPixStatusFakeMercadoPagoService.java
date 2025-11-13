package br.com.five.seven.food.infra.payment.mercadopago.service;

import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.repository.IPaymentCheckoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("${ENVIRONMENT_PROFILE_DEV_NAME}")
public class IPaymentOrderPixStatusFakeMercadoPagoService implements IPaymentCheckoutRepository {

    @Override
    public PaymentStatus getPaymentStatus(String id) {
        return PaymentStatus.APPROVED;
    }

}
