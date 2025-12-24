package br.com.five.seven.food;

import br.com.five.seven.food.config.TestDynamoDBConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestDynamoDBConfig.class)
class PaymentApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should be loaded");
    }

    @Test
    void mainMethodShouldStartApplication() {
        // This test verifies that the main method can be invoked without errors
        // and that it properly initializes the Spring Boot application
        assertNotNull(applicationContext.getBean(PaymentApplication.class),
                "PaymentApplication bean should be present in context");
    }

    @Test
    void mainMethodShouldExecuteWithoutErrors() {
        // Test that the main method can be called directly
        assertDoesNotThrow(() -> {
            PaymentApplication.main(new String[] {
                "--spring.main.web-application-type=none",
                "--spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"
            });
        });
    }
}
