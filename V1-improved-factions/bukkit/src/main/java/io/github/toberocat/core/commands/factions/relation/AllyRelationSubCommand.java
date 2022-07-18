package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AllyRelationSubCommand extends SubCommand {
    public AllyRelationSubCommand() {
        super("ally", "relation.ally", "command.relation.ally.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction addressedFaction = FactionManager.getFactionByRegistry(args[0]);
        if (addressedFaction == null) {
            sendCommandExecuteError("&cCannot find given faction. Check spelling", player);
            return;
        }

        Faction playerFaction = FactionManager.getPlayerFaction(player);

        if (addressedFaction.getRegistryName().equals(playerFaction.getRegistryName())) {
            Language.sendMessage("command.relation.ally.fail", player,
                    new Parseable("{faction}", addressedFaction.getDisplayName()));
            return;
        }
        Result result = playerFaction.getRelationManager().inviteAlly(addressedFaction);

        if (result.isSuccess()) Language.sendMessage("command.relation.ally.success", player,
                new Parseable("{faction}", addressedFaction.getDisplayName()));
        else {
            sendCommandExecuteError(result.getPlayerMessage(), player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        Faction faction = FactionManager.getPlayerFaction(player);
        LinkedList<String> str = new LinkedList<>(FactionManager.getAllFactions());

        return str.stream().filter(x -> !x.equals(faction.getRegistryName())).toList();
    }
}
