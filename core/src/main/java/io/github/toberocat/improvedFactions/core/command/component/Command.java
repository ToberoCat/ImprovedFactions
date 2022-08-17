package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Command<P extends Command.CommandPacket> {
    @NotNull String label();

    @NotNull String permission();

    void description(Function<Translatable, String> query);

    void run(@NotNull P packet);

    interface CommandPacket {

    }
}
