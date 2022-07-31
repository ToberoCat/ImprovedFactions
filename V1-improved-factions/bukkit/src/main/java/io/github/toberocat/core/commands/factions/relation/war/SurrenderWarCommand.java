package io.github.toberocat.core.commands.factions.relation.war;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class SurrenderWarCommand extends SubCommand {
    public SurrenderWarCommand() {
        super("surrender", "war.surrender",
                "command.war.surrender.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setArgLength(1)
                .setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        try {
            Faction<?> playerFaction = FactionHandler.getFaction(player);
            Faction<?> warFaction = FactionHandler.getFaction(args[0]);

            playerFaction.resetRelation(warFaction);
            warFaction.resetRelation(playerFaction);

            // Notify players
            Language.sendMessage("command.war.surrender.added", player,
                    new Parseable("{faction}", warFaction.getDisplay()));

            playerFaction.broadcastTranslatable("command.war.surrender.broadcast",
                    new Parseable("{faction}", warFaction.getDisplay()));
            warFaction.broadcastTranslatable("command.war.surrender.broadcast",
                    new Parseable("{faction}", playerFaction.getDisplay()));
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.war.surrender.faction-not-found", player,
                    new Parseable("{faction}", e.getRegistry()));
        } catch (FactionIsFrozenException e) {
            Language.sendMessage("command.war.surrender.faction-frozen", player,
                    new Parseable("{faction}", e.getRegistry()));
        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            return faction.getEnemies().toList();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace(); // This shouldn't happen
            return List.of("error");
        }
    }
}
