package io.github.toberocat.core.commands.admin.maxpower;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class AdminGivePowerCommand extends SubCommand {
    public AdminGivePowerCommand() {
        super("give", "admin.maxpower.add", "command.admin.maxpower.add.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(2);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        if (!Utility.isNumber(args[1])) return;
        int amount = Integer.parseInt(args[1]);

        Faction faction = FactionManager.getFactionByRegistry(args[0]);

        faction.getPowerManager().setMaxPower(faction.getPowerManager().getMaxPower() + amount);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        LinkedList<String> tab = new LinkedList<>();
        if (args.length <= 1) tab.addAll(FactionManager.getAllFactions());
        else tab.add("<amount>");

        return tab;
    }
}
