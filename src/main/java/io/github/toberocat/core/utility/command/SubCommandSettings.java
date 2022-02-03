package io.github.toberocat.core.utility.command;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.factions.Faction;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class SubCommandSettings {

    public enum NYI { No, Yes, Ignore }

    private String factionPermission;
    private boolean needsAdmin;
    private NYI needsFaction;
    private boolean allowAliases;
    private int argLength;
    private String rank;
    private boolean canUseInConsole;

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

    public boolean canDisplay(SubCommand subCommand, Player player, String[] args, boolean messages) {
        if (player == null && !canUseInConsole) {
            if (messages) MainIF.LogMessage(Level.INFO, "&cYou can't use this command in the console");
            return false;
        }

        return getFactionNYI(subCommand, player, messages);
    }

    private boolean getFactionNYI(SubCommand subCommand, Player player, boolean messages) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        if (needsFaction == NYI.No && faction != null) {
            if (messages) subCommand.SendCommandExecuteError(SubCommand.CommandExecuteError.NoFactionNeed, player);
            return false;
        }
        if (needsFaction == NYI.Yes && faction == null) {
            if (messages) subCommand.SendCommandExecuteError(SubCommand.CommandExecuteError.NoFaction, player);
            return false;
        }
        return true;
    }

    public boolean canExecute(SubCommand subCommand, Player player, String[] args, boolean messages) {
        if (player == null && !canUseInConsole) {
            if (messages) MainIF.LogMessage(Level.INFO, "&cYou can't use this command in the console");
            return false;
        }
        if (argLength != -1 && args.length != argLength) {
            if (messages) subCommand.SendCommandExecuteError(SubCommand.CommandExecuteError.NotEnoughArgs, player);
            return false;
        }

        return getFactionNYI(subCommand, player, messages);

        /*
        Faction faction = FactionUtils.getFaction(player);
        boolean result = true;


        if (args.length != argLength) {
            result = false;
            if (messages) subCommand.CommandExecuteError(SubCommand.CommandExecuteError.NotEnoughArgs, player);
        }

        if (needsFaction == NYI.No && faction != null) {
            result = false;
            if (messages) subCommand.CommandExecuteError(SubCommand.CommandExecuteError.NoFactionNeed, player);
        }
        if (needsFaction == NYI.Yes && faction == null) {
            result = false;
            if (messages) subCommand.CommandExecuteError(SubCommand.CommandExecuteError.NoFaction, player);
        }
        if (faction != null) {
            if (needsAdmin && !FactionUtils.getPlayerRank(faction, player).isAdmin()) {
                result = false;
                if (messages) subCommand.CommandExecuteError(SubCommand.CommandExecuteError.OnlyAdminCommand, player);
            }
            if (factionPermission != null && !faction.hasPermission(player, factionPermission)) {
                result = false;
                if (messages) subCommand.CommandExecuteError(SubCommand.CommandExecuteError.NoFactionPermission, player);
            }
        }

        if (eventCall != null) {
            try {
                boolean eventCall = Utils.CallEvent(getEventCall(), faction, eventParameters, isCancellable);
                result = eventCall && result;
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return result;
         */
    }
}
