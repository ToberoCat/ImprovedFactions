package io.github.toberocat.improvedfactions.commands.factionCommands.claimCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class ClaimOneChunkSubCommand extends SubCommand {
    public ClaimOneChunkSubCommand() {
        super("one", "claim.one", LangMessage.CLAIM_ONE_DESCRIPTION);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setAllowAliases(true)
                .setNeedsAdmin(false)
                .setFactionPermission(Faction.CLAIM_CHUNK_PERMISSION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Utils.ClaimChunk(player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
