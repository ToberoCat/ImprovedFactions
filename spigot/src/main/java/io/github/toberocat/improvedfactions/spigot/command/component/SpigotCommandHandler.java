package io.github.toberocat.improvedfactions.spigot.command.component;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SpigotCommandHandler {

    private final Map<String, Command<?>> lookup;

    public SpigotCommandHandler(@NotNull Command<?> base) {
        this.lookup = new HashMap<>();

        call(base);
    }

    private void call(@NotNull Command<?> command) {
        lookup.putAll(command.getCommands());
        command.getCommands()
                .values()
                .forEach(this::call);
    }

    public @Nullable Command<?> findCommand(@NotNull String query) {
        String[] split = query.split(" ");
        for (int i = split.length - 1; i > 0; i--) {
            Command<?> cmd = lookup.get(split[i]);
            if (cmd != null) return cmd;
        }

        return null;
    }
}
