package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Command<P extends Command.CommandPacket, C extends Command.ConsoleCommandPacket> {

    public static final String PERMISSION_NODE = "faction.command.";

    protected final Map<String, Command<?, ?>> commands = new HashMap<>();
    private CommandSettings settings;

    @NotNull
    public abstract String label();

    protected abstract CommandSettings settings();

    @NotNull
    public String permission() {
        return PERMISSION_NODE + label();
    }

    @NotNull

    public Function<Translatable, String> description() {
        return translatable -> translatable.getMessages().getCommand()
                .get(label())
                .get("description");
    }

    public abstract @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                            @NotNull String[] args);

    public abstract @NotNull List<String> tabCompleteConsole(@NotNull String[] args);

    public abstract void run(@NotNull P packet);

    public abstract void runConsole(@NotNull C packet);

    public abstract @Nullable P createFromArgs(@NotNull FactionPlayer<?> executor,
                                               @NotNull String[] args);

    public abstract @Nullable C createFromArgs(@NotNull String[] args);

    public Map<String, Command<?, ?>> getCommands() {
        return commands;
    }

    public void add(@NotNull Command<?, ?> command) {
        commands.put(command.label(), command);
    }

    public @NotNull CommandSettings readSettings() {
        if (settings == null) settings = settings();
        return settings;
    }

    public interface CommandPacket {
    }

    public interface ConsoleCommandPacket {

    }
}
