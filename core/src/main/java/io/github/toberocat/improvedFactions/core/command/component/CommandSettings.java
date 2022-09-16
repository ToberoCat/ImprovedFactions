package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.permission.Permission;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public class CommandSettings {

    private final Function<Translatable, Map<String, String>> query;

    private boolean allowInConsole;
    private boolean requiresFaction;
    private boolean requiresNoFaction;
    private int requiresRankPriority;
    private String requiresRank;
    private String requiredSpigotPermission;
    private Permission requiredFactionPermission;

    public CommandSettings(@NotNull Function<Translatable, Map<String, String>> node) {
        allowInConsole = false;
        requiresFaction = false;
        requiresNoFaction = false;
        requiresRankPriority = -1;
        requiredSpigotPermission = null;
        requiredFactionPermission = null;
        requiresRank = null;
        query = node;
    }

    public CommandSettings setAllowInConsole(boolean allowInConsole) {
        this.allowInConsole = allowInConsole;
        return this;
    }

    public CommandSettings setRequiresFaction(boolean requiresFaction) {
        this.requiresFaction = requiresFaction;
        this.requiresNoFaction = false;
        return this;
    }

    public CommandSettings setRequiresNoFaction(boolean requiresNoFaction) {
        this.requiresNoFaction = requiresNoFaction;
        this.requiresFaction = false;
        return this;
    }

    public CommandSettings setRequiresRankPriority(int requiresRankPriority) {
        this.requiresRankPriority = requiresRankPriority;
        return this;
    }

    public CommandSettings setRequiredFactionPermission(Permission requiredFactionPermission) {
        this.requiredFactionPermission = requiredFactionPermission;
        return this;
    }

    public CommandSettings setRequiredSpigotPermission(String requiredSpigotPermission) {
        this.requiredSpigotPermission = requiredSpigotPermission;
        return this;
    }

    public CommandSettings setRequiresRank(String requiresRank) {
        this.requiresRank = requiresRank;
        return this;
    }

    public boolean showTab(@NotNull FactionPlayer<?> player) {
        return check(player).result;
    }

    public boolean showTabConsole() {
        return allowInConsole;
    }

    private @NotNull SettingResult faction(@NotNull FactionPlayer<?> player) {
        String registry = player.getFactionRegistry();
        if (registry == null) return new SettingResult(false,
                query.andThen(map -> map.get("requires-faction")));

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            player.getDataContainer().remove(PersistentHandler.FACTION_KEY);
            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get("command-settings")
                    .get("resetted-faction-registry"));
            return new SettingResult(false,
                    query.andThen(map -> map.get("requires-faction")));
        }

        Rank rank = faction.getPlayerRank(player);
        int priority = Rank.getPriority(rank);
        if (priority != -1 && priority < requiresRankPriority) return new SettingResult(false,
                query.andThen(map -> map.get("not-enough-priority")));

        if (requiresRank != null && !requiresRank.equals(rank.getRegistry()))
            return new SettingResult(false, query.andThen(map -> map.get("wrong-rank")));

        return new SettingResult(requiredFactionPermission == null ||
                faction.hasPermission(requiredFactionPermission, rank), query.andThen(map ->
                map.get("missing-faction-permissions")));
    }

    private boolean showNoFaction(@NotNull FactionPlayer<?> player) {
        return true;
    }

    public @NotNull SettingResult canExecute(@NotNull FactionPlayer<?> player) {
        return check(player);
    }

    private @NotNull SettingResult check(@NotNull FactionPlayer<?> player) {
        if (requiredSpigotPermission != null && !player.hasPermission(requiredSpigotPermission))
            return new SettingResult(false, query.andThen(map -> map.get("missing-spigot-permission")));

        boolean hasFaction = player.inFaction();
        if (requiresFaction) return faction(player);
        else if (requiresNoFaction) return new SettingResult(!hasFaction && showNoFaction(player),
                query.andThen(map -> map.get("requires-no-faction")));

        return new SettingResult(true, null);
    }

    public boolean canExecuteConsole() {
        return true;
    }

    public record SettingResult(boolean result, @Nullable Function<Translatable, String> errorMessage) {

    }
}
