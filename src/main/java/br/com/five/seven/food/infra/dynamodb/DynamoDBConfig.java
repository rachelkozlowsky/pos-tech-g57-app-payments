package br.com.five.seven.food.infra.dynamodb;

import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Slf4j
@Configuration
public class DynamoDBConfig {

    @Value("${aws.region:us-east-1}")
    private String amazonAWSRegion;

    @Value("${aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
    @Profile("prd")
    public DynamoDbClient dynamoDbClient() {
        log.info("========================================");
        log.info("Using DynamoDB PRODUCTION configuration");
        log.info("Region: {}", amazonAWSRegion);
        log.info("========================================");

        return DynamoDbClient.builder()
                .region(Region.of(amazonAWSRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(amazonAWSAccessKey, amazonAWSSecretKey)))
                .build();
    }

    @Bean
    @Profile("local & !test")
    public DynamoDbClient dynamoDbClientLocal(@Value("${aws.dynamodb.endpoint}") String amazonDynamoDBEndpoint) {
        log.info("========================================");
        log.info("Using DynamoDB LOCAL configuration");
        log.info("Endpoint: {}", amazonDynamoDBEndpoint);
        log.info("Region: {}", amazonAWSRegion);
        log.info("========================================");

        DynamoDbClient client = DynamoDbClient.builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .region(Region.of(amazonAWSRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(amazonAWSAccessKey, amazonAWSSecretKey)))
                .build();

        try {
            log.info("Testing DynamoDB connection...");
            var tables = client.listTables();
            log.info("Connected successfully! Available tables: {}", tables.tableNames());

            // Test describe table
            var describeResponse = client.describeTable(r -> r.tableName("payment_order_entity"));
            log.info("Table payment_order_entity status: {}", describeResponse.table().tableStatus());
        } catch (Exception e) {
            log.error("Failed to connect to DynamoDB: {}", e.getMessage(), e);
        }

        return client;
    }

    @Bean
    @Profile("!test")
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        log.info("Creating DynamoDbEnhancedClient with custom DynamoDbClient");
        log.info("DynamoDbClient class: {}", dynamoDbClient.getClass().getName());
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    @Profile("!test")
    public DynamoDbTemplate dynamoDbTemplate(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        log.info("Creating DynamoDbTemplate with EnhancedClient");
        DynamoDbTemplate template = new DynamoDbTemplate(dynamoDbEnhancedClient);
        log.info("DynamoDbTemplate created successfully");
        return template;
    }
}
