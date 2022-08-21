package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandSettings {
    private boolean allowInConsole;
    private boolean requiresFaction;
    private boolean requiresNoFaction;
    private int requiresRankPriority;
    private String requiredFactionPermission;
    private String requiredSpigotPermission;

    public boolean isAllowInConsole() {
        return allowInConsole;
    }

    public CommandSettings setAllowInConsole(boolean allowInConsole) {
        this.allowInConsole = allowInConsole;
        return this;
    }

    public boolean isRequiresFaction() {
        return requiresFaction;
    }

    public CommandSettings setRequiresFaction(boolean requiresFaction) {
        this.requiresFaction = requiresFaction;
        this.requiresNoFaction = false;
        return this;
    }

    public boolean isRequiresNoFaction() {
        return requiresNoFaction;
    }

    public CommandSettings setRequiresNoFaction(boolean requiresNoFaction) {
        this.requiresNoFaction = requiresNoFaction;
        this.requiresFaction = false;
        return this;
    }

    public int getRequiresRankPriority() {
        return requiresRankPriority;
    }

    public CommandSettings setRequiresRankPriority(int requiresRankPriority) {
        this.requiresRankPriority = requiresRankPriority;
        return this;
    }

    public String getRequiredFactionPermission() {
        return requiredFactionPermission;
    }

    public CommandSettings setRequiredFactionPermission(String requiredFactionPermission) {
        this.requiredFactionPermission = requiredFactionPermission;
        return this;
    }

    public String getRequiredSpigotPermission() {
        return requiredSpigotPermission;
    }

    public CommandSettings setRequiredSpigotPermission(String requiredSpigotPermission) {
        this.requiredSpigotPermission = requiredSpigotPermission;
        return this;
    }

    public boolean showTab(@NotNull FactionPlayer<?> player) {
        boolean hasFaction = player.inFaction();
        if (requiresFaction && !hasFaction) return false;
        if (requiresNoFaction && hasFaction) return false;
    }

    public boolean showTabConsole() {
        if (!allowInConsole) return false;
    }

    private boolean showFaction(@NotNull FactionPlayer<?> player) {
        String registry = player.getFactionRegistry();
        if (registry == null) return false;

        Faction<?> faction = null;
        try {
            faction = FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        }
        if (faction == null) return false;

        Rank rank = faction.getPlayerRank(player);
        int priority = Rank.getPriority(rank);
        if (priority != -1 && priority < requiresRankPriority) return false;

        faction.has
    }

    public boolean canExecute(@NotNull FactionPlayer<?> player) {

    }

    public boolean canExecuteConsole() {
        if (!allowInConsole) return false;
    }
}
