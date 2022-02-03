package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AllyRelationSubCommand extends SubCommand {
    public AllyRelationSubCommand() {
        super("ally", LangMessage.COMMAND_RELATION_ALLY_DESCRIPTION, false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction addressedFaction = FactionUtility.getFactionByRegistry(args[0]);
        if (addressedFaction == null) {
            SendCommandExecuteError(player, "&cCannot find given faction. Check spelling");
            return;
        }

        Faction playerFaction = FactionUtility.getPlayerFaction(player);

        if (addressedFaction.getRegistryName().equals(playerFaction.getRegistryName())) {
            Language.sendMessage(LangMessage.COMMAND_RELATION_ALLY_FAIL, player,
                    new Parseable("{faction}", addressedFaction.getDisplayName()));
            return;
        }
        addressedFaction.getRelationManager().MakeAlly(playerFaction);
        Language.sendMessage(LangMessage.COMMAND_RELATION_ALLY_SUCCESS, player,
                new Parseable("{faction}", addressedFaction.getDisplayName()));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        LinkedList<String> str = new LinkedList<>(FactionUtility.getAllFactions());

        return str.stream().filter(x -> !x.equals(faction.getRegistryName())).toList();
    }
}
