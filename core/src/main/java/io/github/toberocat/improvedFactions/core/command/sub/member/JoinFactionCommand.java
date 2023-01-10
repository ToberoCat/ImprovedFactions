package io.github.toberocat.improvedFactions.core.command.sub.member;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsAlreadyInFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsBannedException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.OpenType;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.RankContainers;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.*;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class JoinFactionCommand extends Command<JoinFactionCommand.JoinPacket, JoinFactionCommand.JoinPacket> {

    public static final String LABEL = "join";

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
                .setRequiresNoFaction(true)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return getJoinableFactions();
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return ImprovedFactions.api().listPlayers().getOnlinePlayerNames().toList();
        else if (args.length <= 2) return getJoinableFactions();
        else return RankContainers.DEFAULT_FACTION_RANKS_CONTAINERS;
    }

    @Override
    public void run(@NotNull JoinPacket packet) {
        FactionPlayer<?> player = packet.player;
        try {
            packet.faction.joinPlayer(player, packet.rank);
            player.sendMessage(node.andThen(map -> map.get("success")));
        } catch (FactionIsFrozenException e) {
            player.sendMessage(node.andThen(map -> map.get("faction-frozen")));
        } catch (PlayerIsAlreadyInFactionException e) {
            player.sendMessage(node.andThen(map -> map.get("already-in-faction")));
        } catch (PlayerIsBannedException e) {
            player.sendMessage(node.andThen(map -> map.get("sender-banned")));
        }
    }

    @Override
    public void runConsole(@NotNull JoinPacket packet) {
        FactionPlayer<?> player = packet.player;
        Logger logger = Logger.api();
        try {
            packet.faction.joinPlayer(player, packet.rank);

            logger.logInfo("Player joined faction");
            player.sendMessage(node.andThen(map -> map.get("success")));
        } catch (FactionIsFrozenException e) {
            logger.logInfo("Faction is frozen");
        } catch (PlayerIsAlreadyInFactionException e) {
            logger.logInfo("Player is already in faction");
        } catch (PlayerIsBannedException e) {
            logger.logInfo("Player is banned from faction");
        }
    }

    @Override
    public @Nullable JoinFactionCommand.JoinPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                  @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendMessage(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }
        String registry = args[0];

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            executor.sendMessage(node.andThen(map -> map.get("faction-not-found")));
            return null;
        }

        if (faction.getType() != OpenType.PUBLIC) {
            executor.sendMessage(node.andThen(map -> map.get("faction-not-joinable")));
            return null;
        }

        return new JoinPacket(executor, faction, (FactionRank) Rank.fromString(FactionMemberRank.REGISTRY));
    }

    @Override
    public @Nullable JoinFactionCommand.JoinPacket createFromArgs(@NotNull String[] args) {
        if (args.length != 3) {
            Logger.api().logInfo("You need to give a sender, a faction and a rank");
            return null;
        }
        String playerName = args[0];
        FactionPlayer<?> executor = ImprovedFactions.api().getPlayer(playerName);
        if (executor == null) {
            Logger.api().logInfo("Player wasn't found");
            return null;
        }

        String registry = args[1];

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction not joined");
            return null;
        }

        if (faction.getType() != OpenType.PUBLIC) {
            Logger.api().logInfo("Faction isn't public");
            return null;
        }

        Rank rank = Rank.fromString(args[2]);
        if (!(rank instanceof FactionRank factionRank)) {
            Logger.api().logInfo("Couldn't find faction rank");
            return null;
        }

        return new JoinPacket(executor, faction, factionRank);
    }

    private @NotNull List<String> getJoinableFactions() {
        return FactionHandler
                .getAllFactions()
                .map(x -> {
                    try {
                        return FactionHandler.getFaction(x);
                    } catch (FactionNotInStorage e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(x -> x.getType() == OpenType.PUBLIC)
                .map(Faction::getRegistry)
                .toList();
    }

    protected record JoinPacket(@NotNull FactionPlayer<?> player,
                                @NotNull Faction<?> faction,
                                @NotNull FactionRank rank)
            implements Command.CommandPacket, Command.ConsoleCommandPacket {

    }
}
