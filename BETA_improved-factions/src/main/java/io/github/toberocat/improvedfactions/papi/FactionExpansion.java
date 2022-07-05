package io.github.toberocat.improvedfactions.papi;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.utility.callbacks.ResultCallback;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FactionExpansion extends PlaceholderExpansion {

    public static Map<String, Placeholder> PLACEHOLDERS = new HashMap<>();

    public static void init() {
        PLACEHOLDERS.put("name", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return ImprovedFactionsMain.getPlugin().
                    getConfig().getString("general.noFactionPapi");

            return faction.getDisplayName();
        });
        PLACEHOLDERS.put("registry", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return ImprovedFactionsMain.getPlugin().
                    getConfig().getString("general.noFactionPapi");

            return faction.getRegistryName();
        });
        PLACEHOLDERS.put("members_online", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+FactionUtils.getMembersOnline(faction).size();
        });
        PLACEHOLDERS.put("members", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+FactionUtils.getAllPlayers(faction).size();
        });
        PLACEHOLDERS.put("power", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+faction.getPowerManager().getPower();
        });
        PLACEHOLDERS.put("maxpower", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+faction.getPowerManager().getMaxPower();
        });
        PLACEHOLDERS.put("balance", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+faction.getBank().balance().balance;
        });
        PLACEHOLDERS.put("claims", (player) -> {
            Faction faction = FactionUtils.getFaction(player.getUniqueId());
            if (faction == null) return "";

            return ""+faction.getClaimedChunks();
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
        return ImprovedFactionsMain.getVERSION();
    }
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (!PLACEHOLDERS.containsKey(params)) return null;
        return PLACEHOLDERS.get(params).call(player);
    }
}
