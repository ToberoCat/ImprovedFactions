package io.github.toberocat.improvedFactions.core.command.sub.faction.settings;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MotdCommand extends Command<MotdCommand.MotdPacket, MotdCommand.MotdConsolePacket> {

    private static final List<String> motdTabComplete = List.of("<motd>");

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "motd";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return FactionHandler.getAllFactions().toList();
        return motdTabComplete;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return motdTabComplete;
    }

    @Override
    public void run(@NotNull MotdPacket packet) {
        Faction<?> faction;
        try {
            faction = packet.player.getFaction();
        } catch (PlayerHasNoFactionException e) {
            packet.player.sendTranslatable(node.andThen(map -> "player-has-no-faction"));
            return;
        } catch (FactionNotInStorage e) {
            packet.player.sendTranslatable(node.andThen(map -> "faction-not-in-storage"));
            return;
        }

        if (packet.motd == null)
            packet.player.sendTranslatable(node.andThen(map -> map.get("send-motd")));
        else {
            try {
                if (!faction.hasPermission(FactionPermission.SET_MOTD, packet.player)) packet
                        .player
                        .sendTranslatable(node
                                .andThen(map -> map.get("missing-set-motd-permissions")));
                else {
                    faction.setMotd(packet.motd);
                    packet.player.sendTranslatable(node
                            .andThen(map -> map.get("set-motd")));
                }
            } catch (FactionIsFrozenException e) {
                packet.player.sendTranslatable(node
                        .andThen(map -> "faction-is-frozen"));
            } catch (FactionNotInStorage factionNotInStorage) {
                packet.player.sendTranslatable(node
                        .andThen(map -> map.get("faction-not-in-storage")));
            } catch (PlayerHasNoFactionException e) {
                packet.player.sendTranslatable(node
                        .andThen(map -> map.get("player-has-no-faction")));
            }
        }
    }

    @Override
    public void runConsole(@NotNull MotdConsolePacket packet) {
        if (packet.motd == null)
            Logger.api().logInfo(packet.faction.getMotd());
        else {
            boolean frozen = packet.faction.isFrozen();
            packet.faction.setFrozen(false);

            try {
                packet.faction.setMotd(packet.motd);
                Logger.api().logInfo("Updated faction motd");
            } catch (FactionIsFrozenException e) {
                Logger.api().logInfo("Faction is frozen");
            } finally {
                packet.faction.setFrozen(frozen);
            }
        }
    }

    @Override
    public @Nullable MotdPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                               @NotNull String[] args) {
        if (args.length <= 1) return new MotdPacket(executor, null);
        return new MotdPacket(executor, String.join(" ", args));
    }

    @Override
    public @Nullable MotdConsolePacket createFromArgs(@NotNull String[] args) {
        if (args.length <= 1) {
            Logger.api().logInfo("Not enough args. Requires a faction and a new name");
            return null;
        }

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[0]);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction not found");
            return null;
        }

        if (args.length <= 2) return new MotdConsolePacket(faction, null);
        return new MotdConsolePacket(faction, Arrays.stream(args)
                .skip(1)
                .collect(Collectors.joining("")));
    }

    protected static record MotdPacket(@NotNull FactionPlayer<?> player, @Nullable String motd)
            implements Command.CommandPacket {

    }

    protected static record MotdConsolePacket(@NotNull Faction<?> faction, @Nullable String motd)
            implements Command.ConsoleCommandPacket {

    }
}
