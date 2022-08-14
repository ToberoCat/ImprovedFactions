package io.github.toberocat.improvedFactions.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class FileAccess {
    public static final String FACTION_FOLDER = "Factions";
    public static final String CHUNKS_FOLDER = "Chunks";
    public static final String PLAYERS_FOLDER = "Players";

    private final @NotNull File parent;

    public FileAccess(@NotNull File parent) {
        this.parent = parent;

        if (!parent.exists()) parent.mkdirs();
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

    public @NotNull File getFile(String... relativePath) {
        return new File(parent, String.join(File.separator, relativePath));
    }
}
