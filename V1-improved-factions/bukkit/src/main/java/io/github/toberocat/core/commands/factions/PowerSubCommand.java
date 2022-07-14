package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;

import java.util.List;

public class PowerSubCommand extends SubCommand {
    public PowerSubCommand() {
        super("power", "command.power.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        LocalFaction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        Parser.run("command.power.success")
                .parse("{power}", ""+faction.getPowerManager().getCurrentPower())
                .send(player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
