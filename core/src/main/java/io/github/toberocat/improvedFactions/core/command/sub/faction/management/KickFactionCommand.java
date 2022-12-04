package io.github.toberocat.improvedFactions.core.command.sub.faction.management;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerNotAMember;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedFactions.core.utils.faction.RankUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class KickFactionCommand extends
        Command<KickFactionCommand.KickPacket, KickFactionCommand.KickConsolePacket> {

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "kick";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(true)
                .setRequiresFaction(true)
                .setRequiredSpigotPermission(permission())
                .setRequiredFactionPermission(FactionPermission.KICK_PLAYER);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return FactionHandler.getAllFactions().toList();

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[0]);
        } catch (FactionNotInStorage e) {
            return Collections.emptyList();
        }

        return CUtils.mapToPlayerNames(faction.getMembers());
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        Faction<?> faction;
        try {
            faction = player.getFaction();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return Collections.emptyList();
        }

        return CUtils.mapToPlayerNames(RankUtils
                .getManageablePlayers(faction, player));
    }

    @Override
    public void run(@NotNull KickPacket packet) {
        try {
            packet.executor.getFaction().kickPlayer(packet.kicked);
            packet.executor.sendTranslatable(node.andThen(map -> map.get("kicked-sender")));
        } catch (FactionIsFrozenException e) {
            packet.executor.sendTranslatable(node.andThen(map -> map.get("faction-is-frozen")));
        } catch (PlayerHasNoFactionException e) {
            packet.executor.sendTranslatable(node.andThen(map -> map.get("sender-has-no-faction")));
        } catch (FactionNotInStorage e) {
            packet.executor.sendTranslatable(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerNotAMember e) {
            packet.executor.sendTranslatable(node.andThen(map -> map.get("kicked-not-in-faction")));
        }
    }

    @Override
    public void runConsole(@NotNull KickConsolePacket packet) {
        try {
            packet.faction.kickPlayer(packet.kicked);
            Logger.api().logInfo("Kicked sender");
        } catch (FactionIsFrozenException e) {
            Logger.api().logInfo("Faction is frozen");
        } catch (PlayerNotAMember e) {
            Logger.api().logInfo("Player isn't a member");
        }
    }

    @Override
    public @Nullable KickFactionCommand.KickPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                  @NotNull String[] args) {
        if (args.length <= 1) {
            executor.sendTranslatable(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }

        OfflineFactionPlayer<?> player = ImprovedFactions.api().getOfflinePlayer(args[1]);
        if (player == null) {
            executor.sendTranslatable(node.andThen(map -> map.get("sender-not-found")));
            return null;
        }

        return new KickPacket(executor, player);
    }

    @Override
    public @Nullable KickFactionCommand.KickConsolePacket createFromArgs(@NotNull String[] args) {
        if (args.length <= 2) {
            Logger.api().logInfo("You need a faction and the sender you want to kick");
            return null;
        }

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[0]);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction not found");
            return null;
        }

        OfflineFactionPlayer<?> player = ImprovedFactions.api().getOfflinePlayer(args[1]);
        if (player == null) {
            return null;
        }

        return new KickConsolePacket(faction, player);
    }

    protected record KickPacket(@NotNull FactionPlayer<?> executor,
                                @NotNull OfflineFactionPlayer<?> kicked)
            implements Command.CommandPacket {

    }

    protected record KickConsolePacket(@NotNull Faction<?> faction,
                                       @NotNull OfflineFactionPlayer<?> kicked)
            implements ConsoleCommandPacket {
    }
}
