package io.github.toberocat.improvedfactions.commands.factionCommands.relations;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AllySubCommand extends SubCommand {
    public AllySubCommand() {
        super("ally", "");
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

        if (faction.getRegistryName().equals(requestedFaction.getRegistryName())) {
            player.sendMessage(Language.getPrefix() + Language.format("&fCannot be allied with yourself"));
            return;

        }

        if (requestedFaction.getRelationManager().getAllies().contains(faction.getRegistryName())) {
            player.sendMessage(Language.getPrefix() + Language.format("&fYou are already allies"));
            return;
        }

        requestedFaction.getRelationManager().sendInvite(faction);
        player.sendMessage(Language.getPrefix() + Language.format("&fSent &e" + requestedFaction.getDisplayName() + "&f a ally request"));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        Faction faction = FactionUtils.getFaction(player);
        ArrayList<String> registryList = new ArrayList<>();

        for (Faction f : Faction.getFACTIONS()) {
            if (!f.getRegistryName().equals(faction.getRegistryName()))
                registryList.add(f.getRegistryName());
        }
        registryList.removeAll(FactionUtils.getFaction(player).getRelationManager().getAllies());
        return registryList;
    }
}
