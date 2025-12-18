package br.com.five.seven.food.infra.persistence.dynamodb.repository;

import br.com.five.seven.food.infra.persistence.dynamodb.entity.PaymentOrderEntity;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class PaymentOrderDynamoDBRepository {

    private final DynamoDbTable<PaymentOrderEntity> table;
    private static final String ORDER_ID_INDEX = "orderId-index";

    public PaymentOrderDynamoDBRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("PaymentOrderEntity", TableSchema.fromBean(PaymentOrderEntity.class));
        log.info("PaymentOrderDynamoDBRepository initialized with DynamoDbTable");
    }

    public PaymentOrderEntity save(PaymentOrderEntity paymentOrder) {
        table.putItem(paymentOrder);
        return paymentOrder;
    }

    public Optional<PaymentOrderEntity> findById(String id) {
        log.debug("Finding payment by id: {}", id);
        try {
            PaymentOrderEntity result = table.getItem(Key.builder().partitionValue(id).build());
            return Optional.ofNullable(result);
        } catch (Exception e) {
            log.error("Error finding payment by id {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public List<PaymentOrderEntity> findAll() {
        return table.scan().items().stream().toList();
    }

    public List<PaymentOrderEntity> findByOrderId(String orderId) {
        QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(orderId).build()))
                .build();

        return table.index(ORDER_ID_INDEX).query(request).stream()
                .flatMap(page -> page.items().stream())
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
        table.deleteItem(paymentOrder);
    }

    public void deleteById(String id) {
        findById(id).ifPresent(table::deleteItem);
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }
}
