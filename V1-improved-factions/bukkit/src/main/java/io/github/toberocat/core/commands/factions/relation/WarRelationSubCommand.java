package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class WarRelationSubCommand extends SubCommand {
    public WarRelationSubCommand() {
        super("war", "relation.war", "command.relation.enemy.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Faction addressedFaction = FactionManager.getFactionByRegistry(args[0]);
        if (addressedFaction == null) {
            sendCommandExecuteError("&cCannot find given faction. Check spelling", player);
            return;
        }

        Faction playerFaction = FactionManager.getPlayerFaction(player);

        if (addressedFaction.getRegistryName().equals(playerFaction.getRegistryName())) {
            Language.sendMessage("command.relation.war.fail", player,
                    new Parseable("{faction}", addressedFaction.getDisplayName()));
            return;
        }
        addressedFaction.getRelationManager().MakeEnemy(playerFaction);
        Language.sendMessage("command.relation.war.success", player,
                new Parseable("{faction}", addressedFaction.getDisplayName()));
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
