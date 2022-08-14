package io.github.toberocat.improvedFactions.database;

import org.jetbrains.annotations.NotNull;

public record DatabaseVar(String from, String to) {

    public static DatabaseVar of(@NotNull String from, @NotNull String to) {
        return new DatabaseVar(String.format("@%s", from), to);
    }

    public static DatabaseVar of(@NotNull String from, int to) {
        return new DatabaseVar(String.format("@%s", from), String.valueOf(to));
    }

    public static DatabaseVar of(@NotNull String from, long to) {
        return new DatabaseVar(String.format("@%s", from), String.valueOf(to));
    }

    public static DatabaseVar of(@NotNull String from, boolean to) {
        return new DatabaseVar(String.format("@%s", from), String.valueOf(to));
    }

}
