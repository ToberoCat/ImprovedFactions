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
import java.util.Set;

public class ListFactionCommand extends
        Command<Command.PlayerPacket, Command.ConsolePacket> {

    public static final String LABEL = "list";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    public boolean isAdmin() {
        return false;
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
    public void run(@NotNull Command.PlayerPacket packet) {
        List<String> factions = FactionHandler.getAllFactions().toList();
        Set<String> loaded = FactionHandler.getLoadedFactions().keySet();
        if (factions.size() == 0) {
            packet.player().sendTranslatable(node.andThen(map -> map.get("no-entries")));
        } else {
            factions.forEach(f ->
                    packet.player().sendTranslatable(node.andThen(map -> map.get(
                                    loaded.contains(f) ? "entry-loaded" : "entry-unloaded")),
                            new Placeholder("faction", f)));
            packet.player().sendTranslatable(node.andThen(map -> map.get("lower-text")),
                    new Placeholder("factions", String.valueOf(factions.size())),
                    new Placeholder("loaded",
                            String.valueOf(loaded.size())));
        }
    }

    @Override
    public void runConsole(@NotNull ConsolePacket packet) {
        FactionHandler.getAllFactions().forEach(f -> Logger.api().logInfo("%s", f));
    }

    @Override
    public @Nullable Command.PlayerPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                         @NotNull String[] args) {
        return new Command.PlayerPacket(executor);
    }

    @Override
    public @Nullable ConsolePacket createFromArgs(@NotNull String[] args) {
        return new ConsolePacket();
    }
}
