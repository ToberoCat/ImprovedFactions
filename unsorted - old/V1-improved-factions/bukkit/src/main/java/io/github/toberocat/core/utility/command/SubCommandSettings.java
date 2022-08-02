package io.github.toberocat.core.utility.command;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import org.bukkit.entity.Player;

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
    private boolean allowInAllWorlds;

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
        allowInAllWorlds = false;
    }

    public boolean disableInNoneFactionWorlds() {
        return !allowInAllWorlds;
    }

    public SubCommandSettings setAllowInAllWorlds(boolean allowInAllWorlds) {
        this.allowInAllWorlds = allowInAllWorlds;
        return this;
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
        try {
            return new SubSettingExecutor(messages, subCommand, player, this).allowTab();
        } catch (FactionNotInStorage | SettingNotFoundException | PlayerHasNoFactionException e) {
            Utility.except(e);
            return false;
        }
    }

    public boolean canExecute(SubCommand subCommand, Player player, String[] args, boolean messages) {
        try {
            return new SubSettingExecutor(messages, subCommand, player, this).allowExecution(args);
        } catch (FactionNotInStorage | PlayerHasNoFactionException | SettingNotFoundException e) {
            Utility.except(e);
            return false;
        }
    }

    public enum NYI {No, Yes, Ignore}
}
