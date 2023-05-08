package io.github.toberocat.improvedFactions.core.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class YmlManager {
    private static final ObjectMapper YML_MAPPER = new YAMLMapper();

    public static <T> void write(@NotNull T t, @NotNull File file) throws IOException {
        YML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, t);
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull URL url) throws IOException {
        String xml = new Scanner(url.openStream(), StandardCharsets.UTF_8).next();

        return YML_MAPPER.readValue(xml, clazz);
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull File file) throws IOException {
        String xml = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        return YML_MAPPER.readValue(xml, clazz);
    }

}
