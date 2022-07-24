package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
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
        try {
            Faction<?> faction = FactionHandler.getFaction(args[0]);

            faction.setFrozen(false);
            faction.deleteFaction();
            Language.sendMessage("command.admin.disband.success", player,
                    new Parseable("{faction_display}", faction.getDisplay()));
        } catch (FactionNotInStorage e) {
            Language.sendRawMessage("&cCan't find given faction", player);
        } catch (FactionIsFrozenException e) {
            Language.sendMessage("command.admin.disband.frozen", player);
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return FactionHandler.getAllFactions().toList();
    }
}
