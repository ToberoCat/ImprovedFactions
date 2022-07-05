package io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimOneChunkSubCommand extends SubCommand {
    public UnclaimOneChunkSubCommand() {
        super("one", "unclaim.one", LangMessage.UNCLAIM_ONE_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Utils.UnClaimChunk(player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
