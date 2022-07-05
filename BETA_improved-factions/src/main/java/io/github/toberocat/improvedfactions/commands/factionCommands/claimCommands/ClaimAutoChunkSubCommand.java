package io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.*;

public class ClaimAutoChunkSubCommand extends SubCommand {

    public static List<UUID> autoClaim = new ArrayList<>();
    public ClaimAutoChunkSubCommand() {
        super("auto", "claim.auto", LangMessage.AUTO_CLAIM_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) != null) {
            if (autoClaim.contains(player.getUniqueId())) {
                disable(player);
            } else {
                UnclaimAutoChunkSubCommand.disable(player);
                autoClaim.add(player.getUniqueId());
            }

            if (autoClaim.contains(player.getUniqueId())) {
                Language.sendMessage(LangMessage.AUTO_CLAIM_ENABLED, player);
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoFaction, player);
        }
    }

    public static void disable(Player player) {
        if (!autoClaim.contains(player.getUniqueId())) return;

        Language.sendMessage(LangMessage.AUTO_CLAIM_DISABLED, player);
        autoClaim.remove(player.getUniqueId());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
