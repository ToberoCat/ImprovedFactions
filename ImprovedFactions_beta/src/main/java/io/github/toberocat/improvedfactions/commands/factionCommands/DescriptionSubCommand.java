package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.event.faction.FactionLeaveEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DescriptionSubCommand extends SubCommand {
    public DescriptionSubCommand() {
        super("description", LangMessage.DESCRIPTION_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            if (args.length >= 1) {
                Faction faction = FactionUtils.getFaction(player);

                if (faction.isFrozen()) {
                    CommandExecuteError(CommandExecuteError.Frozen, player);
                    return;
                }

                StringBuilder builder = new StringBuilder();
                for (String arg : args) {
                    builder.append(Language.format(arg) + " ");
                }
                faction.setDescription(builder.toString().trim());
                Language.sendMessage(LangMessage.DESCRIPTION_SUCCESS, player);
            } else {
                CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
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
        if (FactionUtils.getFaction(player) == null) {
            result = false;
        }
        return result;
    }
}
