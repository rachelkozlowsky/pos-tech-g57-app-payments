package br.com.five.seven.food.infra.persistence.dynamodb.repository.impl;

import br.com.five.seven.food.infra.persistence.dynamodb.repository.PaymentOrderDynamoDBRepository;
import br.com.five.seven.food.rest.mapper.PaymentMapper;
import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.domain.model.PaymentOrder;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import br.com.five.seven.food.domain.repository.IPaymentRepository;
import br.com.five.seven.food.infra.exceptions.PaymentOrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryAdapterImpl implements IPaymentRepository {

    private final PaymentOrderDynamoDBRepository paymentRepository;
    private final PaymentMapper paymentOrderMapper;

    @Override
    public List<PaymentOrder> findAll(int page, int size) {
        List<PaymentOrderEntity> allPayments = paymentRepository.findAll();

        int start = page * size;
        int end = Math.min(start + size, allPayments.size());

        if (start >= allPayments.size()) {
            return new ArrayList<>();
        }

        return allPayments.subList(start, end).stream()
                .map(paymentOrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentOrder findById(String id) {
        Optional<PaymentOrderEntity> paymentEntity = paymentRepository.findById(id);
        return paymentEntity.map(paymentOrderMapper::toDomain).orElse(null);
    }

    @Override
    public boolean existsById(String id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public PaymentOrder save(PaymentOrder order) {
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
        }

        PaymentOrderEntity entity = paymentOrderMapper.toEntity(order);
        PaymentOrderEntity savedEntity = paymentRepository.save(entity);
        return paymentOrderMapper.toDomain(savedEntity);
    }

    @Override
    public boolean isPaymentOrderAprovedByOrderId(String idOrder) {
        List<PaymentOrderEntity> payments = paymentRepository.findByOrderId(idOrder);
        return payments.stream()
                .anyMatch(payment -> PaymentStatus.APPROVED.equals(payment.getStatus()));
    }

    @Override
    public boolean existsByorderId(String orderId) {
        List<PaymentOrderEntity> payments = paymentRepository.findByOrderId(orderId);
        return !payments.isEmpty();
    }

    @Override
    public PaymentOrder findByorderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .findAny()
                .map(paymentOrderMapper::toDomain)
                .orElseThrow();
    }

    @Override
    public PaymentOrder update(PaymentOrder paymentOrder) {
        return save(paymentOrder);
    }

    @Override
    public PaymentOrder findByintegrationId(String integrationId) {
        var paymentOrderEntity = paymentRepository.findByIntegrationId(integrationId);

        if (paymentOrderEntity.isEmpty()) {
            throw new PaymentOrderNotFoundException(String.format("Payment Order not found by integrationId: %s",
                    integrationId));
        }
        return paymentOrderMapper.toDomain(paymentOrderEntity.get());
    }

    @Override
    public void deleteById(String paymentId) {
        findById(paymentId);
        paymentRepository.deleteById(paymentId);
    }
}
