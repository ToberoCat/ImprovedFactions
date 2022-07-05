package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands.ClaimAutoChunkSubCommand;
import io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands.ClaimOneChunkSubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Debugger;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimChunkSubCommand extends SubCommand {

    public static List<SubCommand> subCommands = new ArrayList<>();

    public ClaimChunkSubCommand() {
        super("claim", LangMessage.CLAIM_DESCRIPTION);
        subCommands.add(new ClaimOneChunkSubCommand());
        subCommands.add(new ClaimAutoChunkSubCommand());
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsAdmin(false)
                .setAllowAliases(true)
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setFactionPermission(Faction.CLAIM_CHUNK_PERMISSION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtils.getFaction(player);

        if (faction.isFrozen()) {
            CommandExecuteError(CommandExecuteError.Frozen, player);
            return;
        }
        if (args.length == 0) {
            subCommands.get(0).CallSubCommand(player, args);
            return;
        }

        if(!SubCommand.CallSubCommands(subCommands, player, args)) {
            Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return SubCommand.CallSubCommandsTab(subCommands, player, args);
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null)
            result = false;
        else if (!faction.hasPermission(player, Faction.CLAIM_CHUNK_PERMISSION))
            result = false;
        return result;
    }
}
