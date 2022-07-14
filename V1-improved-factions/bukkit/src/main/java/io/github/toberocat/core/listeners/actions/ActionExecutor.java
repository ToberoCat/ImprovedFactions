package io.github.toberocat.core.listeners.actions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.utility.action.Actions;
import io.github.toberocat.core.utility.action.FactionActions;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.events.faction.*;
import io.github.toberocat.core.utility.events.faction.claim.ChunkProtectEvent;
import io.github.toberocat.core.utility.events.faction.claim.ChunkRemoveProtectionEvent;
import io.github.toberocat.core.utility.events.faction.member.FactionMemberOfflineEvent;
import io.github.toberocat.core.utility.events.faction.member.FactionMemberOnlineEvent;
import io.github.toberocat.core.utility.events.faction.power.FactionPowerEvent;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActionExecutor implements Listener {

    private final FileConfiguration actionConfig;

    public ActionExecutor(MainIF plugin) {
        actionConfig = new DataManager(plugin, "actions.yml").getConfig();
    }

    private @NotNull List<String> getAction(@NotNull String path) {
        return actionConfig.getStringList(path);
    }

    private void send(@NotNull String path, @NotNull CommandSender sender, Parseable... parseables) {
        Actions actions = new Actions(getAction(path));
        for (Parseable parseable : parseables) actions.placeholder(parseable.getParse(), parseable.getTo());
        actions.run(sender);
    }

    private void send(@NotNull String path, @NotNull LocalFaction faction, Parseable... parseables) {
        FactionActions actions = new FactionActions(getAction(path));

        for (Parseable parseable : parseables) actions.placeholder(parseable.getParse(), parseable.getTo());
        actions.run(faction);
    }

    @EventHandler
    private void create(FactionCreateEvent event) {
        send("faction.create", event.getPlayer(),
                new Parseable("{faction}", event.getFaction().getDisplayName()));
    }

    @EventHandler
    private void delete(FactionDeleteEvent event) {
        send("faction.delete", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()));
    }

    @EventHandler
    private void ally(FactionAllyEvent event) {
        send("faction.ally", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{allied-faction}", event.getAllied().getDisplayName()));
    }

    @EventHandler
    private void ban(FactionBanEvent event) {
        send("faction.ban", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{banned}", event.getBanned().getName()));
    }

    @EventHandler
    private void join(FactionJoinEvent event) {
        send("faction.join", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{joined}", event.getPlayer().getName()));
    }

    @EventHandler
    private void join(FactionKickEvent event) {
        send("faction.kick", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{kicked}", event.getKicked().getName()));
    }

    @EventHandler
    private void leave(FactionLeaveEvent event) {
        send("faction.leave", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{kicked}", event.getPlayer().getName()));
    }

    @EventHandler
    private void load(FactionLoadEvent event) {
        send("faction.load", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()));
    }

    @EventHandler
    private void memberOnline(FactionMemberOnlineEvent event) {
        send("faction.member.online", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{member}", event.getPlayer().getName()));
    }

    @EventHandler
    private void memberOffline(FactionMemberOfflineEvent event) {
        send("faction.member.offline", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{member}", event.getPlayer().getName()));
    }

    @EventHandler
    private void overclaim(FactionOverclaimEvent event) {
        send("faction.overclaim", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{overclaim-faction}", event.getNewOwners().getDisplayName()),
                new Parseable("{chunk-x}", "" + event.getChunk().getX() * 16),
                new Parseable("{chunk-z}", "" + event.getChunk().getZ() * 16));
    }

    @EventHandler
    private void power(FactionPowerEvent event) {
        switch (event.getCause()) {
            case PLAYER_JOIN -> send("faction.power.player-joined", Bukkit.getConsoleSender(),
                    new Parseable("{faction}", event.getFaction().getDisplayName()));
            case PLAYER_LEFT -> send("faction.power.player-left", Bukkit.getConsoleSender(),
                    new Parseable("{faction}", event.getFaction().getDisplayName()));
            case PLAYER_KILLED -> send("faction.power.player-killed", Bukkit.getConsoleSender(),
                    new Parseable("{faction}", event.getFaction().getDisplayName()));
            case REGENERATION_TICK -> send("faction.power.regeneration-tick", Bukkit.getConsoleSender(),
                    new Parseable("{faction}", event.getFaction().getDisplayName()));
            case MAX_POWER -> send("faction.power.max-power", Bukkit.getConsoleSender(),
                    new Parseable("{faction}", event.getFaction().getDisplayName()));
        }
    }

    @EventHandler
    private void transferOwnership(FactionTransferOwnershipEvent event) {
        send("faction.transfer-ownership", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{old}", event.getCurrentOwner().getDisplayName()),
                new Parseable("{new}", event.getNewOwner().getName()));
    }

    @EventHandler
    private void pardon(FactionUnbanEvent event) {
        send("faction.pardon", Bukkit.getConsoleSender(),
                new Parseable("{faction}", event.getFaction().getDisplayName()),
                new Parseable("{unbanned}", event.getBanned().getName()));
    }

    @EventHandler
    private void updtaeRank(FactionUpdateMemberRankEvent event) {
        OfflinePlayer off = event.getPlayer();
        if (!off.isOnline()) return;
        Player player = off.getPlayer();
        if (player == null) return;

        String oldRank = event.getOldRank();
        String newRank = event.getNewRank();

        if (oldRank == null) {
            send("faction.rank.initial", player, // Initial
                    new Parseable("{faction}", event.getFaction().getDisplayName()),
                    new Parseable("{old-rank}", "no"));
            return;
        }

        Rank old = Rank.fromString(oldRank);
        Rank newR = Rank.fromString(newRank);
        if (old == null || newR == null) return;

        if (old.getRawPriority() < newR.getRawPriority()) {
            send("faction.rank.promotion", player,
                    new Parseable("{faction}", event.getFaction().getDisplayName()),
                    new Parseable("{old-rank}", old.getDisplayName()),
                    new Parseable("{new-rank}", newR.getDisplayName()));
        } else if (old.getRawPriority() > newR.getRawPriority()) {
            send("faction.rank.degradation", player,
                    new Parseable("{faction}", event.getFaction().getDisplayName()),
                    new Parseable("{old-rank}", old.getDisplayName()),
                    new Parseable("{new-rank}", newR.getDisplayName()));
        } else {
            send("faction.rank.initial", player,
                    new Parseable("{faction}", event.getFaction().getDisplayName()),
                    new Parseable("{old-rank}", old.getDisplayName()),
                    new Parseable("{new-rank}", newR.getDisplayName()));
        }
    }

    @EventHandler
    private void protect(ChunkProtectEvent event) {
        send("chunk.protect", Bukkit.getConsoleSender(),
                new Parseable("{registry}", event.getRegistry()),
                new Parseable("{chunk-x}", "" + event.getChunk().getX() * 16),
                new Parseable("{chunk-z}", "" + event.getChunk().getZ() * 16));
    }

    @EventHandler
    private void removeProtection(ChunkRemoveProtectionEvent event) {
        send("chunk.rm-protection", Bukkit.getConsoleSender(),
                new Parseable("{registry}", event.getRegistry()),
                new Parseable("{chunk-x}", "" + event.getChunk().getX() * 16),
                new Parseable("{chunk-z}", "" + event.getChunk().getZ() * 16));
    }
}
