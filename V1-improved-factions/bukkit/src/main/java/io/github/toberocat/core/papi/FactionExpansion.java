package io.github.toberocat.core.papi;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.language.Language;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FactionExpansion extends PlaceholderExpansion {

    public static Map<String, Placeholder> PLACEHOLDERS = new HashMap<>();

    public static void init() {
        PLACEHOLDERS.put("rank", (player) -> {
            try {
                return FactionHandler.getFaction(player).getPlayerRank(player).getDisplayName();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("tag", player -> {
            try {
                return FactionHandler.getFaction(player).getTag();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("motd", (player) -> {
            try {
                return FactionHandler.getFaction(player).getMotd();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("name", player -> {
            try {
                return FactionHandler.getFaction(player).getDisplay();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("registry", player -> {
            try {
                return FactionHandler.getFaction(player).getRegistry();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("members_online", player -> {
            try {
                return String.valueOf(FactionHandler.getFaction(player).getOnlineMembers().count());
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("members", player -> {
            try {
                return String.valueOf(FactionHandler.getFaction(player).getMembers().count());
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("power", (player) -> {
            try {
                return FactionHandler.getFaction(player).getActivePower().toEngineeringString();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });
        PLACEHOLDERS.put("maxpower", player -> {
            try {
                return FactionHandler.getFaction(player).getActiveMaxPower().toEngineeringString();
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
        });

        PLACEHOLDERS.put("claims", player -> {
            try {
                return String.valueOf(FactionHandler.getFaction(player).getClaims().getTotal());
            } catch (PlayerHasNoFactionException e) {
                return Language.getMessage("papi.no-faction", "en_us");
            } catch (FactionNotInStorage e) {
                e.printStackTrace();
                return "error";
            }
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
