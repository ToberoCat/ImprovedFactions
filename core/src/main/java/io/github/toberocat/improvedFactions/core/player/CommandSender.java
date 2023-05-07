package io.github.toberocat.improvedFactions.core.player;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.exceptions.TranslatableRuntimeException;
import org.jetbrains.annotations.NotNull;

public interface CommandSender {
    void runCommand(@NotNull String command);

    boolean hasPermission(@NotNull String permission);

    void sendException(@NotNull TranslatableException e);

    void sendException(@NotNull TranslatableRuntimeException e);

    String getName();
}
