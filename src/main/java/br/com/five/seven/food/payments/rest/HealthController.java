package br.com.five.seven.food.payments.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Health", description = "Health check endpoint")
@RestController
@RequestMapping
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "UP!";
    }
}
