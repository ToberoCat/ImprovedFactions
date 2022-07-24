package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminDisbandSubCommand extends SubCommand {
    public AdminDisbandSubCommand() {
        super("disband", "admin.disband", "command.admin.disband.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Faction faction = FactionManager.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }
        faction.delete();
        Language.sendMessage("command.admin.disband.success", player,
                new Parseable("{faction_display}", faction.getDisplayName()));
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return FactionManager.getAllFactions();
    }
}
