package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
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
    protected Function<Translatable, Map<String, String>> node;

    protected CommandSettings settings;
    protected ConfigHandler config;

    public Command() {
        node = translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label());
        settings = createSettings();
        config = createConfig();

        PermissionFileTool.addPermission(permission(), isAdmin());
    }

    public Command(boolean setManually) {
        if (setManually) return;
        node = translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label());

        settings = createSettings();
        config = createConfig();

        PermissionFileTool.addPermission(permission(), isAdmin());
    }

    protected @NotNull ConfigHandler createConfig() {
        return ImprovedFactions.api().getConfig("commands.yml").getSection(label() + "-command");
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

    @Override
    public String toString() {
        return "Command{" + label() + "}";
    }

    /* Interfaces */

    public interface CommandPacket {
    }

    public interface ConsoleCommandPacket {

    }

    public static record PlayerPacket(FactionPlayer<?> player) implements CommandPacket {

    }

    public static record ConsolePacket() implements ConsoleCommandPacket {

    }
}
