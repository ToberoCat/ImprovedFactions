package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.gui.faction.OnlineGUI;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class OnlineSubCommand extends SubCommand {
    public OnlineSubCommand() {
        super("online", "command.online.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            new OnlineGUI(player, faction, () -> {
            });
        });
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
