package io.github.toberocat.core.commands.factions.relation.ally;

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

public class AllyRemoveCommand extends SubCommand {
    public AllyRemoveCommand() {
        super("remove", "ally.remove",
                "command.ally.remove.description", false);
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
            Faction<?> allied = FactionHandler.getFaction(args[0]);

            playerFaction.resetRelation(allied);
            allied.resetRelation(playerFaction);

            // Notify
            Language.sendMessage("command.ally.remove.removed-ally", player,
                    new Parseable("{faction}", allied.getDisplay()));

            allied.broadcastTranslatable("command.ally.remove.removed-broadcast",
                    new Parseable("{faction}", playerFaction.getDisplay()));
            playerFaction.broadcastTranslatable("command.ally.remove.removed-broadcast",
                    new Parseable("{faction}", allied.getDisplay()));
        } catch (FactionIsFrozenException e) {
            Language.sendMessage("command.ally.remove.faction-frozen", player,
                    new Parseable("{faction}", e.getRegistry()));
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.ally.remove.faction-not-found", player);
        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            return faction.getAllies().toList();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace();
            return List.of("error");
        }
    }
}
