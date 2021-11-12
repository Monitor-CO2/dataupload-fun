package br.edu.ifsc.co2monitor.getx;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple dependency manager for Java, inspired by GetX.
 * May be replaced by Guice or ActiveJ inject in the future.
 */
public class GetX {

    private static Map<Class<?>, Object> instances = new HashMap<>();

    public static void put(Class<?> instanceType, Object instance) {
        instances.put(instanceType, instance);
    }

    public static <T> T find(Class<T> instanceType) {
        return (T) instances.get(instanceType);
    }

}
