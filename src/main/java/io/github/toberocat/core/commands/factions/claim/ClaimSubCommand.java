package io.github.toberocat.core.commands.factions.claim;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

public class ClaimSubCommand extends AutoSubCommand {
    public ClaimSubCommand() {
        super("claim", "command.faction.claim.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    public String getEnabledKey() {
        return "command.faction.claim.auto.enable";
    }

    @Override
    public String getDisabledKey() {
        return "command.faction.claim.auto.disable";
    }

    @Override
    public void onSingle(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        Result result = MainIF.getIF().getClaimManager().claimChunk(faction, player.getLocation().getChunk());

        if (result.isSuccess()) Language.sendMessage("command.faction.claim.success", player);
        else
            Language.sendMessage("command.faction.claim.failed", player,
                    new Parseable("{error}", result.getPlayerMessage()));
    }
}
