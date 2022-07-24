package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;

import java.util.List;

public class AllyAcceptSubCommand extends SubCommand {
    public AllyAcceptSubCommand() {
        super("allyaccept", "relation.allyaccept",
                "command.relation.ally-accept.description", false);
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
        Faction faction = FactionManager.getPlayerFaction(player);
        if (faction == null) return null;

        return faction.getRelationManager().getAllyInvitations();
    }
}
