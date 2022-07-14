package io.github.toberocat.versions.benchmark;

import io.github.toberocat.core.utility.data.annotation.DatabaseField;
import io.github.toberocat.core.utility.ReflectUtility;

import java.lang.reflect.Field;
import java.util.Set;

public class SimpleBenchmark {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Set<Field> fields = ReflectUtility.findFields(ExampleDbStoreable.class, DatabaseField.class);

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");
    }
}
