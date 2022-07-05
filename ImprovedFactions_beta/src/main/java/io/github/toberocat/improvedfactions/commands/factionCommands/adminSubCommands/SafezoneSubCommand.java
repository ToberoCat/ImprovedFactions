package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class SafezoneSubCommand extends SubCommand {
    public SafezoneSubCommand() {
        super("safezone", LangMessage.ADMIN_SAFEZONE_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFactionByRegistry("safezone") == null) {
            Faction faction = Faction.create(player, Language.format(
                    ImprovedFactionsMain.getPlugin().getConfig().getString("general.safezoneText")));
            faction.setRegistryName("safezone");
            faction.getPowerManager().setMaxPower(9999);
            faction.getPowerManager().setPower(9999);
        }
        Utils.ClaimChunk(FactionUtils.getFactionByRegistry("safezone"), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
