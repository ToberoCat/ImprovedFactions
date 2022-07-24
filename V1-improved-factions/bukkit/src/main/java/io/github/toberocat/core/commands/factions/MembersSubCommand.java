package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.gui.faction.MemberGui;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MembersSubCommand extends SubCommand {
    public MembersSubCommand() {
        super("members", "command.members.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> {
            Faction faction = FactionManager.getPlayerFaction(player);
            new MemberGui(player, faction, new GuiSettings());
        });
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
