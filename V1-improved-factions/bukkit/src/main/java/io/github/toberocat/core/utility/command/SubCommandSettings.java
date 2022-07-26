package io.github.toberocat.core.utility.command;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class SubCommandSettings {

    private String factionPermission;
    private boolean needsAdmin;
    private NYI needsFaction;
    private boolean allowAliases;
    private int argLength;
    private String rank;
    private boolean canUseInConsole;
    private boolean useWhenFrozen;
    private Object[] eventParameters;
    private boolean isCancellable;

    public SubCommandSettings() {
        factionPermission = null;
        needsAdmin = false;
        needsFaction = NYI.Ignore;
        allowAliases = true;
        isCancellable = true;
        eventParameters = null;
        argLength = -1;
        canUseInConsole = false;
        useWhenFrozen = false;
    }

    public String getRank() {
        return rank;
    }

    public SubCommandSettings setRank(String rank) {
        this.rank = rank;
        return this;
    }

    public int getArgLength() {
        return argLength;
    }

    public SubCommandSettings setArgLength(int argLength) {
        this.argLength = argLength;
        return this;
    }

    public Object[] getEventParameters() {
        return eventParameters;
    }

    public SubCommandSettings setEventParameters(Object[] eventParameters) {
        this.eventParameters = eventParameters;
        return this;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public SubCommandSettings setCancellable(boolean cancellable) {
        isCancellable = cancellable;
        return this;
    }

    public boolean isAllowAliases() {
        return allowAliases;
    }

    public SubCommandSettings setAllowAliases(boolean allowAliases) {
        this.allowAliases = allowAliases;
        return this;
    }

    public boolean isNeedsAdmin() {
        return needsAdmin;
    }

    public SubCommandSettings setNeedsAdmin(boolean needsAdmin) {
        this.needsAdmin = needsAdmin;
        return this;
    }

    public String getFactionPermission() {
        return factionPermission;
    }

    public SubCommandSettings setFactionPermission(String factionPermission) {
        this.factionPermission = factionPermission;
        return this;
    }

    public NYI getNeedsFaction() {
        return needsFaction;
    }

    public SubCommandSettings setNeedsFaction(NYI needsFaction) {
        this.needsFaction = needsFaction;
        return this;
    }

    public boolean isCanUseInConsole() {
        return canUseInConsole;
    }

    public SubCommandSettings setCanUseInConsole(boolean canUseInConsole) {
        this.canUseInConsole = canUseInConsole;
        return this;
    }

    public boolean isUseWhenFrozen() {
        return useWhenFrozen;
    }

    public SubCommandSettings setUseWhenFrozen(boolean useWhenFrozen) {
        this.useWhenFrozen = useWhenFrozen;
        return this;
    }

    public boolean canDisplay(SubCommand subCommand, Player player, String[] args, boolean messages) {
        return new SubSettingExecutor(messages, subCommand, player, this).allowTab();
    }

    private boolean getFactionOperations(SubCommand subCommand, Player player, boolean messages) {
        if (FactionHandler.isInFaction(player)) return playerInFaction(subCommand, player);
        return playerNoFaction(subCommand, player);

        if (needsFaction == NYI.No && faction != null) {
            if (messages) subCommand.sendCommandExecuteError(SubCommand.CommandExecuteError.NoFactionNeed, player);
            return false;
        }
        if (needsFaction == NYI.Yes && faction == null) {
            if (messages) subCommand.sendCommandExecuteError(SubCommand.CommandExecuteError.NoFaction, player);
            return false;
        }

        if (faction != null && faction.isFrozen() && !useWhenFrozen) {
            if (messages) Language.sendRawMessage("Faction is frozen. You can't use that", player);
            return false;
        }

        if (faction != null && factionPermission != null && !faction.hasPermission(player, factionPermission)) {
            if (messages) Language.sendRawMessage("&cYou don't have enough permissions", player);
            return false;
        }

        return true;
    }

    public boolean canExecute(SubCommand subCommand, Player player, String[] args, boolean messages) {
        return new SubSettingExecutor(messages, subCommand, player, this).allowExecution(args);
    }

    public enum NYI {No, Yes, Ignore}
}
