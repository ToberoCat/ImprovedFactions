package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class AllyCancelCommand extends SubCommand {
    public AllyCancelCommand() {
        super("ally", "ally.cancel",
                "command.ally.cancel.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setArgLength(1);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        try {
            Faction<?> allyCanceller = FactionHandler.getFaction(args[0]);
            Faction<?> playerFaction = FactionHandler.getFaction(player);

            playerFaction.removeAllyInvite(allyCanceller);

            //Notify
            Language.sendMessage("command.ally.cancel.success", player);

            allyCanceller.broadcastTranslatable("command.ally.cancel.broadcast",
                    new Parseable("{faction}", playerFaction.getDisplay()));
            playerFaction.broadcastTranslatable("command.ally.cancel.broadcast",
                    new Parseable("{faction}", allyCanceller.getDisplay()));
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.ally.cancel.faction-not-found", player);
        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace(); // This shouldn't happen
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            return faction.getSentInvites().toList();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace();
            return List.of("error");
        }
    }
}
