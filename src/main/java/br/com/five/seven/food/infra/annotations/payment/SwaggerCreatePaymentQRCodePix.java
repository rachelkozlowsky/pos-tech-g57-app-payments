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
@Operation(summary = "create a pix payment", description = "create a payment and generate a qr code pix")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully created payment"),
        @ApiResponse(responseCode = "400", description = "Payment already approved to the order"),
        @ApiResponse(responseCode = "500", description = "Internal Server error")
})
public @interface SwaggerCreatePaymentQRCodePix {
}
