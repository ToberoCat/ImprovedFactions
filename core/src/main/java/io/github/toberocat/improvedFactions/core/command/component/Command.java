package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.PermissionFileTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class Command<P extends Command.CommandPacket, C extends Command.ConsoleCommandPacket> {

    public static final String PERMISSION_NODE = "faction.command.";

    protected final Map<String, Command<?, ?>> commands = new HashMap<>();
    protected final Function<Translatable, Map<String, String>> node;

    private final CommandSettings settings;
    private final ConfigHandler config;

    public Command() {
        this.node = translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label());
        this.settings = createSettings();
        this.config = config();

        PermissionFileTool.addPermission(permission(), isAdmin());
    }

    public @NotNull String permission() {
        return PERMISSION_NODE + label();
    }

    public abstract boolean isAdmin();

    public @NotNull Function<Translatable, String> description() {
        return translatable -> translatable.getMessages().getCommand()
                .get(label())
                .get("description");
    }

    public @NotNull CommandSettings settings() {
        return settings;
    }

    public @NotNull ConfigHandler config() {
        return config;
    }

    public @NotNull Map<String, Command<?, ?>> getCommands() {
        return commands;
    }

    public void add(@NotNull Command<?, ?> command) {
        commands.put(command.label(), command);
    }

    public abstract @NotNull String label();

    protected abstract @NotNull CommandSettings createSettings();

    /* Tab complete */
    public abstract @NotNull List<String> tabCompleteConsole(@NotNull String[] args);

    public abstract @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                            @NotNull String[] args);

    /* Command execution */
    public abstract void run(@NotNull P packet);

    public abstract void runConsole(@NotNull C packet);

    /* Packet creation */
    public abstract @Nullable P createFromArgs(@NotNull FactionPlayer<?> executor,
                                               @NotNull String[] args);

    public abstract @Nullable C createFromArgs(@NotNull String[] args);

    /* Interfaces */

    public interface CommandPacket {
    }

    public interface ConsoleCommandPacket {

    }
}
