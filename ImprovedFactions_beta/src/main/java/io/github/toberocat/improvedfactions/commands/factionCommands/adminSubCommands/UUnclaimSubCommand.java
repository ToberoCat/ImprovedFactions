package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class UUnclaimSubCommand extends SubCommand {
    public UUnclaimSubCommand() {
        super("uUnclaim", LangMessage.ADMIN_UNCLAIM_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction f = ChunkUtils.GetFactionClaimedChunk(player.getLocation().getChunk());
        if (f != null) {
            Utils.UnClaimChunk(f, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
