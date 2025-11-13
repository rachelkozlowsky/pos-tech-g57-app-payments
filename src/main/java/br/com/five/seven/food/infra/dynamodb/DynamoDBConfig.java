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

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.region:us-east-1}")
    private String amazonAWSRegion;

    @Value("${aws.accesskey:dummykey}")
    private String amazonAWSAccessKey;

    @Value("${aws.secretkey:dummysecret}")
    private String amazonAWSSecretKey;

    @Bean
    @Profile("!des")
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(amazonAWSRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(amazonAWSAccessKey, amazonAWSSecretKey)))
                .build();
    }

    @Bean
    @Profile("des")
    public DynamoDbClient dynamoDbClientLocal() {
        log.info("Using DynamoDB local");
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(amazonDynamoDBEndpoint))
                .region(Region.of(amazonAWSRegion))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(amazonAWSAccessKey, amazonAWSSecretKey)))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTemplate dynamoDbTemplate(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        return new DynamoDbTemplate(dynamoDbEnhancedClient);
    }
}
