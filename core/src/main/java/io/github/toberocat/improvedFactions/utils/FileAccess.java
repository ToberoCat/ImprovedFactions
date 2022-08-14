package io.github.toberocat.improvedFactions.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public record FileAccess(File parent) {
    public static final String FACTION_FOLDER = "Factions";
    public static final String CHUNKS_FOLDER = "Chunks";
    public static final String PLAYERS_FOLDER = "Players";

    private static final ObjectMapper OBJECT_MAPPER = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        return mapper;
    }

    public void delete(String... relativePath) {
        getFile(relativePath).delete();
    }

    public boolean has(String... relativePath) {
        return getFile(relativePath).exists();
    }

    public File[] listFiles(String... relativePath) {
        return getFile(relativePath).listFiles();
    }

    public String[] list(String... relativePath) {
        return getFile(relativePath).list();
    }

    public <T> T read(Class<T> clazz, String... relativePath) throws IOException {
        return OBJECT_MAPPER.readValue(getFile(relativePath), clazz);
    }

    private @NotNull File getFile(String... relativePath) {
        return new File(parent, String.join(File.separator, relativePath));
    }
}
