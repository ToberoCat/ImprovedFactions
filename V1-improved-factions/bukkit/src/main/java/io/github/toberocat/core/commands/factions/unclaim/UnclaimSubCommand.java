package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

public class UnclaimSubCommand extends AutoSubCommand {
    public UnclaimSubCommand() {
        super("unclaim", "command.faction.claim.one.description", false);
    }

    @Override
    public String getEnabledKey() {
        return "command.faction.unclaim.auto.enable";
    }

    @Override
    public String getDisabledKey() {
        return "command.faction.unclaim.auto.disable";
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsFaction(SubCommandSettings.NYI.Yes);
    }


    @Override
    public void onSingle(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        String registry = MainIF.getIF().getClaimManager().getFactionRegistry(player.getLocation().getChunk());
        if (!faction.getRegistryName().equals(registry)) {
            Language.sendMessage("command.faction.unclaim.failed", player,
                    new Parseable("{error}", "Can't unclaim a chunk you aren't owning"));
            return;
        }


        Result result = MainIF.getIF().getClaimManager().removeClaim(faction, player.getLocation().getChunk());

        if (result.isSuccess()) Language.sendMessage("command.faction.unclaim.one.success", player);
        else
            Language.sendMessage("command.faction.unclaim.one.failed", player,
                    new Parseable("{error}", result.getPlayerMessage()));
    }
}
