package io.github.toberocat.core.utility;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectUtility {

    /**
     * @return null safe set
     */
    public static Set<Field> findFields(@NotNull final Class<?> clazz,  @NotNull final Class<? extends Annotation> ann) {
        Set<Field> set = new HashSet<>();
        Class<?> c = clazz;
        while (c != null) {
            Arrays.stream(c.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(ann))
                    .collect(Collectors.toCollection(() -> set));

            c = c.getSuperclass();
        }
        return set;
    }

    public static setField(@NotNull Field field, )
}
