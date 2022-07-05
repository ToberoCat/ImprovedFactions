package io.github.toberocat.improvedfactions.commands.subCommands;

import io.github.toberocat.improvedfactions.event.FactionEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class SubCommandSettings {

    public enum NYI { No, Yes, Ignore };

    private String factionPermission;
    private boolean needsAdmin;
    private NYI needsFaction;
    private boolean allowAliases;

    private Class<FactionEvent> eventCall;
    private Object[] eventParameters;
    private boolean isCancellable;

    public SubCommandSettings() {
        factionPermission = null;
        needsAdmin = false;
        needsFaction = NYI.Ignore;
        allowAliases = true;
        eventCall = null;
        isCancellable = true;
        eventParameters = null;
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

    public Class<FactionEvent> getEventCall() {
        return eventCall;
    }

    public SubCommandSettings setEventCall(Class<FactionEvent> eventCall) {
        this.eventCall = eventCall;
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

    public boolean areConditionsTrue(SubCommand subCommand, Player player, String[] args, boolean messages) {
        Faction faction = FactionUtils.getFaction(player);
        boolean result = true;


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
    }
}
