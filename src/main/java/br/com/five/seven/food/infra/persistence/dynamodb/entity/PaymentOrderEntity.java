package br.com.five.seven.food.infra.persistence.dynamodb.entity;

import br.com.five.seven.food.domain.enums.PaymentOption;
import br.com.five.seven.food.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class PaymentOrderEntity {

    private String id;
    private PaymentStatus status;
    private PaymentOption paymentOption;
    private String orderId;
    private String integrationId;
    private String correlationIntegrationId;
    private BigDecimal amount;
    private String dateApproved;
    private String lastUpdate;
    private String qrData;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"orderId-index"})
    public String getOrderId() {
        return orderId;
    }
}
