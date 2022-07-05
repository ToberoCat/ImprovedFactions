package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.language.Parseable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisbandFactionCommand extends SubCommand {

    public DisbandFactionCommand() {
        super("disband", LangMessage.ADMIN_DISBAND_DESCRIPTION);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings();
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (1 == args.length) {
            Faction foundFaction = FactionUtils.getFactionByRegistry(args[0]);
                if (foundFaction == null) {
                    player.sendMessage(Language.getPrefix() + "§cCouldn't find faction to delete");
                    return;
                }
                if (foundFaction.DeleteFaction()) {
                    Language.sendMessage(LangMessage.DELETE_SUCCESS, player,
                            new Parseable("{faction_displayname}", foundFaction.getDisplayName()));
                } else {
                    CommandExecuteError(CommandExecuteError.OtherError, player);
                }
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> tab = new ArrayList<>();

        if (1 == args.length) {
            for (Faction faction : Faction.getFACTIONS()) {
                tab.add(faction.getRegistryName());
            }
        }
        return tab;
    }
}
