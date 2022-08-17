package io.github.toberocat.improvedFactions.core.sender;

import org.jetbrains.annotations.NotNull;

public interface CommandSender {
    void runCommand(@NotNull String command);

    boolean hasPermission(@NotNull String permission);
}
