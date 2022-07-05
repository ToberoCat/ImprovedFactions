package io.github.toberocat.improvedfactions.commands.factionCommands.relations;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NeutralSubCommand extends SubCommand {
    public NeutralSubCommand() {
        super("neutral", "");
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsAdmin(true).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }
        Faction requestedFaction = FactionUtils.getFactionByRegistry(args[0]);

        if (requestedFaction == null) {
            Language.sendMessage(LangMessage.JOIN_ERROR_NO_FACTION_FOUND, player);
            return;
        }
        Faction faction = FactionUtils.getFaction(player);

        requestedFaction.getRelationManager().neutral(faction);
        faction.getRelationManager().neutral(requestedFaction);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        ArrayList<String> registryList = new ArrayList<>();

        for (Faction faction : Faction.getFACTIONS()) {
            registryList.add(faction.getRegistryName());
        }
        registryList.removeAll(FactionUtils.getFaction(player).getRelationManager().getAllies());
        return registryList;
    }
}
