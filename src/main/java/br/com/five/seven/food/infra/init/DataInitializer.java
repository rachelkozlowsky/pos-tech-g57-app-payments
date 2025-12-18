package br.com.five.seven.food.infra.init;

import br.com.five.seven.food.infra.persistence.dynamodb.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.IndexMetadata;
import software.amazon.awssdk.enhanced.dynamodb.mapper.BeanTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.EnhancedGlobalSecondaryIndex;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

@Slf4j
@Component
@Profile("!local")
public class DataInitializer implements CommandLineRunner {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbEnhancedClient enhancedClient;

    public DataInitializer(DynamoDbClient dynamoDbClient, DynamoDbEnhancedClient enhancedClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.enhancedClient = enhancedClient;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing DynamoDB tables...");

        // Create all required tables
        createTableIfNotExists(PaymentOrderEntity.class, 5L, 5L);

        log.info("DynamoDB tables initialization completed");
    }

    private <T> void createTableIfNotExists(Class<T> entityClass, Long readCapacity, Long writeCapacity) {
        try {
            String tableName = entityClass.getSimpleName();

            // Check if table already exists
            try {
                DescribeTableRequest describeTableRequest = DescribeTableRequest.builder()
                        .tableName(tableName)
                        .build();
                dynamoDbClient.describeTable(describeTableRequest);
                log.info("Table {} already exists", tableName);
            } catch (ResourceNotFoundException e) {
                log.info("Table not found: {}", tableName);
            }
        } catch (Exception e) {
            log.error("Error creating table for {}: {}", entityClass.getSimpleName(), e.getMessage(), e);
        }
    }

    private <T> EnhancedGlobalSecondaryIndex buildEnhancedGlobalSecondaryIndex(BeanTableSchema<T> beanTableSchema, Long readCapacity, Long writeCapacity) {
        return beanTableSchema.tableMetadata().indices().stream()
                .map(IndexMetadata::name)
                .filter(name -> !name.contains("$"))
                .findAny()
                .map(indexName -> EnhancedGlobalSecondaryIndex.builder()
                        .indexName(indexName)
                        .provisionedThroughput(ProvisionedThroughput.builder()
                                .readCapacityUnits(readCapacity)
                                .writeCapacityUnits(writeCapacity)
                                .build())
                        .projection(p -> p.projectionType(ProjectionType.ALL))
                        .build()).orElse(null);
    }

    private void waitForTableToBecomeActive(String tableName) {
        log.info("Waiting for table {} to become ACTIVE...", tableName);

        try (DynamoDbWaiter waiter = DynamoDbWaiter.builder()
                .client(dynamoDbClient)
                .build()) {

            DescribeTableRequest request = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            waiter.waitUntilTableExists(request);
            log.info("Table {} is now ACTIVE", tableName);
        } catch (Exception e) {
            throw new RuntimeException("Error waiting for table " + tableName + " to become active: " + e.getMessage(), e);
        }
    }
}
