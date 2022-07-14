package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
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
        LocalFaction addressedFaction = FactionUtility.getFactionByRegistry(args[0]);
        if (addressedFaction == null) {
            sendCommandExecuteError("&cCannot find given faction. Check spelling", player);
            return;
        }

        LocalFaction playerFaction = FactionUtility.getPlayerFaction(player);

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
        LocalFaction faction = FactionUtility.getPlayerFaction(player);
        LinkedList<String> str = new LinkedList<>(FactionUtility.getAllFactions());

        return str.stream().filter(x -> !x.equals(faction.getRegistryName())).toList();
    }
}
