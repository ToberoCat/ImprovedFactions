package io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnclaimAutoChunkSubCommand extends SubCommand {

    public static List<UUID> autoUnclaim = new ArrayList<UUID>();

    public UnclaimAutoChunkSubCommand() {
        super("auto", "unclaim.auto", LangMessage.AUTO_UNCLAIM_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            if (autoUnclaim.contains(player.getUniqueId())) {
                disable(player);
            } else {
                ClaimAutoChunkSubCommand.disable(player);
                autoUnclaim.add(player.getUniqueId());
            }
            if (autoUnclaim.contains(player.getUniqueId())) {
                Language.sendMessage(LangMessage.AUTO_UNCLAIM_ENABLED, player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoFaction, player);
        }
    }

    public static void disable(Player player) {
        if (!autoUnclaim.contains(player.getUniqueId())) return;
        autoUnclaim.remove(player.getUniqueId());
        Language.sendMessage(LangMessage.AUTO_UNCLAIM_DISABLED, player);

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
