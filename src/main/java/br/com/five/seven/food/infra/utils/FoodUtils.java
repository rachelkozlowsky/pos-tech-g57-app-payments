package br.com.five.seven.food.infra.utils;

public class FoodUtils {

    public static String limparString(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replaceAll("[^0-9]", "");
    }
}
