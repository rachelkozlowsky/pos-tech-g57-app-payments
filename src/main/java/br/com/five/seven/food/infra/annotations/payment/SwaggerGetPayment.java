package br.com.five.seven.food.infra.annotations.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(summary = "get list payments", description = "Retrieve a list payments paged")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the list payments"),
        @ApiResponse(responseCode = "500", description = "Internal Server error")
})
public @interface SwaggerGetPayment {
}
