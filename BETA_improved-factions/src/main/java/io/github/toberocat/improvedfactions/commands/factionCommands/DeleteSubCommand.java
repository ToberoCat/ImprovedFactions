package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.event.faction.FactionDeleteEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.language.Parseable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteSubCommand extends SubCommand {
    public DeleteSubCommand() {
        super("delete", LangMessage.DELETE_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            Faction faction = FactionUtils.getFaction(player);

            if (faction.isFrozen()) {
                CommandExecuteError(CommandExecuteError.Frozen, player);
                return;
            }

            if (FactionUtils.getPlayerRank(faction, player).isAdmin()) {
                FactionDeleteEvent deleteEvent = new FactionDeleteEvent(faction, player);
                Bukkit.getPluginManager().callEvent(deleteEvent);
                if (faction.DeleteFaction() && !deleteEvent.isCancelled()) {
                    Language.sendMessage(LangMessage.DELETE_SUCCESS, player,
                            new Parseable("{faction_displayname}", faction.getDisplayName()));
                } else {
                    if (deleteEvent.isCancelled())
                        Language.sendMessage(LangMessage.DELETE_ERROR, player,
                                new Parseable("{error}", deleteEvent.getCancelMessage()));
                    else
                        CommandExecuteError(CommandExecuteError.OtherError, player);
                }
            } else {
                CommandExecuteError(CommandExecuteError.OnlyAdminCommand, player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoFaction, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null) {
            result = false;
        }
        if (faction != null && FactionUtils.getPlayerRank(faction, player) != null && !FactionUtils.getPlayerRank(faction, player).isAdmin())
            result = false;
        return result;
    }
}
