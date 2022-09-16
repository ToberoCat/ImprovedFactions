package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static @NotNull String parse(@NotNull Object item) throws JsonProcessingException {
        return mapper.writeValueAsString(item);
    }

    public static @NotNull Object parse(@NotNull Class<?> clazz, @Nullable String item)
            throws JsonProcessingException {
        return mapper.readValue(item, clazz);
    }
}
