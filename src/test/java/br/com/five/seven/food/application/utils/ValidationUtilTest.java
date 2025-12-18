package br.com.five.seven.food.application.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationUtilTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "12345678909",
        "98765432100",
        "11122233396",
        "00000000191"
    })
    void validarCPFWithMultipleValidCPFsShouldReturnTrue(String cpf) {
        assertTrue(ValidationUtil.validarCPF(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "11111111111",
        "22222222222",
        "33333333333",
        "44444444444",
        "55555555555",
        "66666666666",
        "77777777777",
        "88888888888",
        "99999999999",
        "00000000000"
    })
    void validarCPFWithRepeatedDigitsShouldReturnFalse(String cpf) {
        assertFalse(ValidationUtil.validarCPF(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "john@gmail.com",
        "maria@yahoo.com.br",
        "admin@company.org",
        "support@help.net",
        "info@business.co.uk"
    })
    void validarEmailWithMultipleValidEmailsShouldReturnTrue(String email) {
        assertTrue(ValidationUtil.validarEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "plainaddress",
        "@missingatdomain.com",
        "spaces @domain.com",
        "user@",
        "@domain.com",
        "user@@domain.com",
        "user@domain@com",
    })
    void validarEmailWithMultipleInvalidEmailsShouldReturnFalse(String email) {
        assertFalse(ValidationUtil.validarEmail(email));
    }
}
