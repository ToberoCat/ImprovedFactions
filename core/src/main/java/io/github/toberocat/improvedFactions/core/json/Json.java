package io.github.toberocat.improvedFactions.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.toberocat.improvedFactions.core.gui.ItemContainer;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new SimpleModule()
                    .addKeySerializer(ItemContainer.class, new MapKeySerializer())
                    .addKeyDeserializer(ItemContainer.class, new MapKeyDeserializer()));


    public static void writeToFile(@NotNull File file, @NotNull Object item) throws IOException {
        mapper.writeValue(file, item);
    }

    public static @NotNull String parse(@NotNull Object item) throws JsonProcessingException {
        return mapper.writeValueAsString(item);
    }

    public static @NotNull Object parse(@NotNull Class<?> clazz, @Nullable String item)
            throws JsonProcessingException {
        return mapper.readValue(item, clazz);
    }

    public static @NotNull <T> T parse(@NotNull Class<T> clazz, @NotNull File file) throws IOException {
        return mapper.readValue(file, clazz);
    }
}
