package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CreateFactionCommand implements Command<CreateFactionCommand.CreateFactionPacket> {
    @Override
    public @NotNull String label() {
        return null;
    }

    @Override
    public @NotNull String permission() {
        return null;
    }

    @Override
    public void description(Function<Translatable, String> query) {

    }

    @Override
    public void run(@NotNull CreateFactionPacket packet) {

    }

    public static class CreateFactionPacket implements CommandPacket {

    }
}
