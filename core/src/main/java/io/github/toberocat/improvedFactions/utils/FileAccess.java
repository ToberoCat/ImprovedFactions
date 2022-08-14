package io.github.toberocat.improvedFactions.utils;

import java.io.File;

public record FileAccess(File parent) {
    public static final String FACTION_FOLDER = "Factions";
    public static final String CHUNKS_FOLDER = "Chunks";
    public static final String PLAYERS_FOLDER = "Players";

    public void delete(String... relativePath) {
        new File(parent, String.join(File.separator, relativePath)).delete();
    }
}
