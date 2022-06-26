package io.github.toberocat.core.papi;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FactionExpansion extends PlaceholderExpansion {

    public static Map<String, Placeholder> PLACEHOLDERS = new HashMap<>();



    public static void init() {
        PLACEHOLDERS.put("rank", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return faction.getPlayerRank(offlinePlayer).getDisplayName();
        });
        PLACEHOLDERS.put("tag", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return faction.getTag();
        });
        PLACEHOLDERS.put("motd", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return faction.getMotd();
        });
        PLACEHOLDERS.put("name", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return faction.getDisplayName();
        });
        PLACEHOLDERS.put("registry", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return faction.getRegistryName();
        });
        PLACEHOLDERS.put("members_online", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return "" + faction.getFactionMemberManager().getMembers().stream().filter(x ->
                    Bukkit.getOfflinePlayer(x).isOnline()).toList().size();
        });
        PLACEHOLDERS.put("members", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return "" + faction.getFactionMemberManager().getMembers().size();
        });
        PLACEHOLDERS.put("power", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);


            return "" + faction.getPowerManager().getCurrentPower();
        });
        PLACEHOLDERS.put("maxpower", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return "" + faction.getPowerManager().getMaxPower();
        });
        PLACEHOLDERS.put("balance", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return "" + faction.getFactionBank().balance().balance;
        });
        PLACEHOLDERS.put("claims", (offlinePlayer) -> {
            if (!offlinePlayer.isOnline()) return null;

            Player player = offlinePlayer.getPlayer();
            if (player == null) return null;

            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return Language.getMessage("papi.no-faction", player);

            return "" + faction.getClaimedChunks();
        });
    }

    @Override
    public @NotNull String getIdentifier() {
        return "faction";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Tobero";
    }

    @Override
    public @NotNull String getVersion() {
        return MainIF.getVersion().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (!PLACEHOLDERS.containsKey(params)) return null;
        return PLACEHOLDERS.get(params).call(player);
    }
}
