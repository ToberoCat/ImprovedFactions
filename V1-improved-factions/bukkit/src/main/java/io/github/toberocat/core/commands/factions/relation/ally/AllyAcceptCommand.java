package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;

import java.util.List;

public class AllyAcceptCommand extends SubCommand {
    public AllyAcceptCommand() {
        super("accept", "ally.accept",
                "command.ally.accept.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setNeedsFaction(SubCommandSettings.NYI.Yes)
                .setArgLength(1);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Faction faction = FactionManager.getPlayerFaction(player);
        if (faction == null) return;
        if (!faction.getRelationManager().getAllyInvitations().contains(args[0])) {
            Parser.run("command.relation.ally-accept.no-invite")
                    .parse("{faction}", args[0])
                    .send(player);
            return;
        }

        Faction other = FactionManager.getFactionByRegistry(args[0]);
        if (other == null) return;

        faction.getRelationManager().acceptInvite(other);
        Parser.run("command.relation.ally-accept.accept")
                .parse("{faction}", args[0])
                .send(player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(player);
            return faction.getReceivedInvites().toList();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace();
            return List.of("error");
        }
    }
}
