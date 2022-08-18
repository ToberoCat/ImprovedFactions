package io.github.toberocat.improvedfactions.spigot;

import org.jetbrains.annotations.NotNull;

public class Test {
    public static void main(String[] args) throws Exception {
    }

    static @NotNull String validateDisplay(@NotNull String display) {
        return display
                .substring(0, 10);
    }
}
