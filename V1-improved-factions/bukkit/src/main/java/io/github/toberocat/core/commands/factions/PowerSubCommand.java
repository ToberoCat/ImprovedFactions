package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
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
    protected void commandExecute(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            Parser.run("command.power.success")
                    .parse("{power}", "" + faction.getActivePower().toEngineeringString())
                    .send(player);
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
