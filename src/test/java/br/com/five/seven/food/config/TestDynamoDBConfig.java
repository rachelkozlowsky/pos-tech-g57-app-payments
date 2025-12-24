package br.com.five.seven.food.config;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestDynamoDBConfig {

    @Bean
    @Primary
    public DynamoDbClient dynamoDbClient() {
        return mock(DynamoDbClient.class);
    }

    @Bean
    @Primary
    public DynamoDbEnhancedClient dynamoDbEnhancedClient() {
        return mock(DynamoDbEnhancedClient.class);
    }

    @Bean
    @Primary
    public DynamoDbTemplate dynamoDbTemplate() {
        return mock(DynamoDbTemplate.class);
    }
}
