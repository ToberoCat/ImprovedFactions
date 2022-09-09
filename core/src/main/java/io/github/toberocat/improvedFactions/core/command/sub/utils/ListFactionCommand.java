package io.github.toberocat.improvedFactions.core.command.sub.utils;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ListFactionCommand extends
        Command<ListFactionCommand.ListPacket, ListFactionCommand.ListConsolePacket> {

    public static final String LABEL = "list";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull ListPacket packet) {
        List<String> factions = FactionHandler.getAllFactions().toList();
        if (factions.size() == 0) {
            packet.receiver.sendTranslatable(node.andThen(map -> map.get("no-entries")));
        } else factions.forEach(f -> packet.receiver.sendTranslatable(node.andThen(map -> map.get("entry")),
                        new Placeholder("{faction}", f)));
    }

    @Override
    public void runConsole(@NotNull ListConsolePacket packet) {
        FactionHandler.getAllFactions().forEach(f -> Logger.api().logInfo("%s", f));
    }

    @Override
    public @Nullable ListFactionCommand.ListPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                  @NotNull String[] args) {
        return new ListPacket(executor);
    }

    @Override
    public @Nullable ListFactionCommand.ListConsolePacket createFromArgs(@NotNull String[] args) {
        return new ListConsolePacket();
    }

    protected record ListPacket(@NotNull FactionPlayer<?> receiver)
            implements CommandPacket {
    }

    protected record ListConsolePacket() implements ConsoleCommandPacket {

    }
}
