package io.github.toberocat.improvedFactions.translator;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.toberocat.improvedFactions.translator.layout.meta.contributor.BetaTesterContributor;
import io.github.toberocat.improvedFactions.translator.layout.meta.contributor.DeveloperContributor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;

public class XmlLoader {
    private static final XmlMapper XML_MAPPER = createMapper();

    private static @NotNull XmlMapper createMapper() {
        XmlMapper mapper = new XmlMapper();

        return mapper;
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull URL url) throws IOException {
        String xml = new Scanner(url.openStream(), StandardCharsets.UTF_8)
                .useDelimiter("\\A").next();

        return XML_MAPPER.readValue(xml, clazz);
    }

    public static  <T> T read(@NotNull Class<T> clazz, @NotNull File file) throws IOException {
        String xml = Files.readString(file.toPath(), StandardCharsets.US_ASCII);

        return XML_MAPPER.readValue(Files.readString(file.toPath(), StandardCharsets.US_ASCII), clazz);
    }

}
