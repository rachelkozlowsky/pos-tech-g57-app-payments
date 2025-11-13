package br.com.five.seven.food.infra.persistence.dynamodb.repository;

import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PaymentOrderDynamoDBRepository {

    private final DynamoDbTemplate dynamoDbTemplate;
    private static final String ORDER_ID_INDEX = "orderId-index";

    public PaymentOrderDynamoDBRepository(DynamoDbTemplate dynamoDbTemplate) {
        this.dynamoDbTemplate = dynamoDbTemplate;
    }

    public PaymentOrderEntity save(PaymentOrderEntity paymentOrder) {
        dynamoDbTemplate.save(paymentOrder);
        return paymentOrder;
    }

    public Optional<PaymentOrderEntity> findById(String id) {
        return Optional.ofNullable(dynamoDbTemplate.load(Key.builder().partitionValue(id).build(), PaymentOrderEntity.class));
    }

    public List<PaymentOrderEntity> findAll() {
        return dynamoDbTemplate.scanAll(PaymentOrderEntity.class).items().stream().toList();
    }

    public List<PaymentOrderEntity> findByOrderId(String orderId) {
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(orderId).build()))
                .build();

        return dynamoDbTemplate.query(request, PaymentOrderEntity.class, ORDER_ID_INDEX)
                .items()
                .stream()
                .toList();
    }

    public List<PaymentOrderEntity> findByStatus(PaymentStatus status) {
        return findAll().stream()
                .filter(payment -> status.equals(payment.getStatus()))
                .collect(Collectors.toList());
    }

    public Optional<PaymentOrderEntity> findByIntegrationId(String integrationId) {
        return findAll().stream()
                .filter(payment -> integrationId.equals(payment.getIntegrationId()))
                .findFirst();
    }

    public void delete(PaymentOrderEntity paymentOrder) {
        dynamoDbTemplate.delete(paymentOrder);
    }

    public void deleteById(String id) {
        findById(id).ifPresent(dynamoDbTemplate::delete);
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }
}
