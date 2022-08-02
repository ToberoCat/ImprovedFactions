package io.github.toberocat.core.commands.factions.relation.ally;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.faction.relation.FactionHasntInvitedException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        try {
            Faction<?> faction = FactionHandler.getFaction(args[0]);
            Faction<?> playerFaction = FactionHandler.getFaction(player);
            acceptInvite(faction, playerFaction);

            // Notify
            Parser.run("command.ally.accept.accepted")
                    .parse("{faction}", faction.getDisplay())
                    .send(player);

            faction.broadcastTranslatable("command.ally.accept.broadcast",
                    new Parseable("{faction}", faction.getDisplay()));

            playerFaction.broadcastTranslatable("command.ally.accept.broadcast",
                    new Parseable("{faction}", playerFaction.getDisplay()));
        } catch (FactionIsFrozenException e) {
            Parser.run("command.ally.accept.faction-frozen")
                    .parse("{faction}", e.getRegistry())
                    .send(player);
        } catch (FactionNotInStorage e) {
            Language.sendMessage("command.ally.accept.faction-not-found", player);
        } catch (FactionHasntInvitedException e) {
            Language.sendMessage("command.ally.accept.no-invite", player);
        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace(); // This shouldn't happen
        }
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

    private void acceptInvite(@NotNull Faction<?> inviter, @NotNull Faction<?> invited)
            throws FactionHasntInvitedException, FactionIsFrozenException {
        if (!inviter.hasInvited(invited)) throw new FactionHasntInvitedException(inviter, invited);

        inviter.addAlly(invited);
        invited.addAlly(inviter);

        inviter.removeAllyInvite(invited);
    }
}
