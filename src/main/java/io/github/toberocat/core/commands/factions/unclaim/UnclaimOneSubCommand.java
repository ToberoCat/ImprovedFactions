package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimOneSubCommand extends SubCommand {
    public UnclaimOneSubCommand() {
        super("one", "unclaim.one", LangMessage.COMMAND_FACTION_CLAIM_DESCRIPTION, false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        unclaim(player);
    }

    public static void unclaim(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        Result result = MainIF.getIF().getClaimManager().claimChunk(faction, player.getLocation().getChunk());

        if (result.isSuccess()) Language.sendMessage(LangMessage.COMMAND_FACTION_CLAIM_ONE_SUCCESS, player);
        else
            Language.sendMessage(LangMessage.COMMAND_FACTION_CLAIM_ONE_FAILED, player,
                    new Parseable("{error}", result.getPlayerMessage()));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
