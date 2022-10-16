package io.github.toberocat.improvedFactions.core.command.sub.utils;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WhoFactionCommand
        extends Command<WhoFactionCommand.WhoPacket, Command.ConsoleCommandPacket> {
    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "who";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return FactionHandler.getAllFactions().toList();
    }

    @Override
    public void run(@NotNull WhoPacket packet) {
        String[] messages = packet.player.getMessageBatch(node.andThen(map -> map
                .keySet()
                .stream()
                .filter(x -> x.contains("who-entry"))
                .sorted(Comparator.comparingInt(this::get))
                .toArray(String[]::new)));
        for (String message : messages) {
            packet.player.sendTranslatable(node.andThen(map -> map.get(message)),
                    new Placeholder("{faction-name}", packet.faction.getDisplay()),
                    new Placeholder("{faction-motd}", packet.faction.getMotd()),
                    new Placeholder("{faction-registry}", packet.faction.getRegistry()),
                    new Placeholder("{faction-owner}", Objects.requireNonNull(ImprovedFactions
                                    .api()
                                    .getOfflinePlayer(packet.faction.getOwner()))
                            .getName()),
                    new Placeholder("{faction-online}", "" + packet.faction.getOnlineMembers().count()),
                    new Placeholder("{faction-members}", "" + packet.faction.getMembers().count()),
                    new Placeholder("{faction-power}", "" + packet.faction.getTotalPower()),
                    new Placeholder("{faction-members}", "" + packet.faction.getTotalMaxPower()),
                    new Placeholder("{faction-description}", "" + packet.faction
                            .getDescription()
                            .getLines()
                            .collect(Collectors.joining("\n")))
            );
        }
    }

    protected int get(@NotNull String o) {
        String[] parts = o.split("-");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    @Override

    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable WhoFactionCommand.WhoPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        try {
            if (args.length == 0)
                return new WhoPacket(executor, executor.getFaction());
            return new WhoPacket(executor, FactionHandler.getFaction(args[0]));
        } catch (FactionNotInStorage e) {
            executor.sendTranslatable(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerHasNoFactionException e) {
            executor.sendTranslatable(node.andThen(map -> map.get("player-has-no-faction")));
        }

        return null;
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }


    protected record WhoPacket(@NotNull FactionPlayer<?> player, @NotNull Faction<?> faction)
            implements CommandPacket {

    }
}
