package br.com.five.seven.food.utils;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class TestUtils {
    public static <T> T dummyObject(Class<T> clazz) {
        EasyRandomParameters parameters = new EasyRandomParameters().ignoreRandomizationErrors(true);
        EasyRandom easyRandom = new EasyRandom(parameters);
        return easyRandom.nextObject(clazz);
    }
}