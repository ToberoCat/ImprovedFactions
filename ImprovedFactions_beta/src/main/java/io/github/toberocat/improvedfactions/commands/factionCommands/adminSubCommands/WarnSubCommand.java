package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class WarnSubCommand extends SubCommand {
    public WarnSubCommand() {
        super("warn", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) return;
        Faction faction = FactionUtils.getFactionByRegistry(args[0]);
        int result = ImprovedFactionsMain.WARNS.addWarn(faction);

        if (result >= 5) {
            Language.sendRawMessage("Faction got disband due to it having more than 5 warnings", player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Faction.getFACTIONS().stream().map(Faction::getRegistryName).toList();
    }
}
