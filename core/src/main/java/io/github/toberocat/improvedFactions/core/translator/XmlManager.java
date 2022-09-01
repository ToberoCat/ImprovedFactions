package io.github.toberocat.improvedFactions.core.translator;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class XmlManager {
    private static final XmlMapper XML_MAPPER = createMapper();

    private static @NotNull XmlMapper createMapper() {
        XmlMapper mapper = new XmlMapper();

        return mapper;
    }

    public static <T> void write(@NotNull T t, @NotNull File file) throws IOException {
        XML_MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, t);
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull URL url) throws IOException {
        String xml = new Scanner(url.openStream(), StandardCharsets.UTF_8)
                .useDelimiter("\\A").next();

        return XML_MAPPER.readValue(xml, clazz);
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull File file) throws IOException {
        String xml = Files.readString(file.toPath(), StandardCharsets.UTF_8);

        return XML_MAPPER.readValue(xml, clazz);
    }

}
