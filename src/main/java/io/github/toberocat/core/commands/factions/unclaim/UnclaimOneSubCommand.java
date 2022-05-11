package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimOneSubCommand extends SubCommand {
    public UnclaimOneSubCommand() {
        super("one", "unclaim.one", "command.faction.claim.one.description", false);
    }

    public static void unclaim(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        Result result = MainIF.getIF().getClaimManager().claimChunk(faction, player.getLocation().getChunk());

        if (result.isSuccess()) Language.sendMessage("command.faction.claim.one.success", player);
        else
            Language.sendMessage("command.faction.claim.one.failed", player,
                    new Parseable("{error}", result.getPlayerMessage()));
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        unclaim(player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
