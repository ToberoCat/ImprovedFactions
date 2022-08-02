package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.FactionOwnerIsOfflineException;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.core.utility.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllyInviteCommand extends SubCommand {

    public AllyInviteCommand() {
        super("invite", "ally.invite",
                "command.ally.invite.description", false);
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
            Faction<?> faction = FactionHandler.getFaction(player);
            Faction<?> invited = FactionHandler.getFaction(args[0]);
            faction.inviteAlly(invited);

            // Notify players
            Parser.run("command.ally.invite.invited")
                    .parse("{faction}", invited.getRegistry())
                    .send(player);

            faction.broadcastTranslatable("command.ally.invite.broadcast",
                    new Parseable("{player}", player.getName()),
                    new Parseable("{faction}", invited.getDisplay()));

        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace(); // This shouldn't happen, due to it being a requirement
        } catch (CantInviteYourselfException e) {
            Language.sendMessage("command.ally.invite.cant-invite-yourself", player);
        } catch (FactionOwnerIsOfflineException e) {
            Language.sendMessage("command.ally.invite.faction-owner-offline", player);
        } catch (FactionNotInStorage e) {
            Parser.run("command.ally.invite.faction-not-found")
                    .parse("{faction}", args[0])
                    .send(player);
        } catch (FactionIsFrozenException e) {
            Parser.run("command.ally.invite.frozen")
                    .parse("{faction}", e.getRegistry())
                    .send(player);
        } catch (AlreadyInvitedException e) {
            Language.sendMessage("command.ally.invite.already-invited", player);
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        try {
            String registry = FactionHandler.getFaction(player).getRegistry();
            return FactionHandler.getAllFactions()
                    .filter(x -> !x.equals(registry))
                    .toList();
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}