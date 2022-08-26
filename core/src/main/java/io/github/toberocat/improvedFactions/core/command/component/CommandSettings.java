package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class CommandSettings {
    private boolean allowInConsole;
    private boolean requiresFaction;
    private boolean requiresNoFaction;
    private int requiresRankPriority;
    private String requiresRank;
    private String requiredFactionPermission;
    private String requiredSpigotPermission;

    public CommandSettings() {
        allowInConsole = false;
        requiresFaction = false;
        requiresNoFaction = false;
        requiresRankPriority = -1;
        requiredSpigotPermission = null;
        requiredFactionPermission = null;
        requiresRank = null;
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

    public CommandSettings setRequiredFactionPermission(String requiredFactionPermission) {
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
        if (requiredSpigotPermission != null &&
                !player.hasPermission(requiredSpigotPermission)) return false;

        boolean hasFaction = player.inFaction();
        if (requiresFaction) return hasFaction && faction(player);
        else if (requiresNoFaction) return !hasFaction && showNoFaction(player);

        return true;
    }

    public boolean showTabConsole() {
        return allowInConsole;
    }

    private boolean faction(@NotNull FactionPlayer<?> player) {
        String registry = player.getFactionRegistry();
        if (registry == null) return false;

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
            return false;
        }

        Rank rank = faction.getPlayerRank(player);
        int priority = Rank.getPriority(rank);
        if (priority != -1 && priority < requiresRankPriority) return false;

        if (requiresRank != null && !requiresRank.equals(rank.getRegistry()))
            return false;

        return requiredFactionPermission == null ||
                faction.hasPermission(requiredFactionPermission, rank);
    }

    private boolean showNoFaction(@NotNull FactionPlayer<?> player) {
        return true;
    }

    public boolean canExecute(@NotNull FactionPlayer<?> player) {
        if (requiredSpigotPermission != null &&
                !player.hasPermission(requiredSpigotPermission)) return false;

        boolean hasFaction = player.inFaction();
        if (requiresFaction) return hasFaction && faction(player);
        else if (requiresNoFaction) return !hasFaction && showNoFaction(player);

        return true;
    }

    public boolean canExecuteConsole() {
        return true;
    }
}
