package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.invite.AllyInvite;
import io.github.toberocat.core.invite.InviteHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.FactionOwnerIsOfflineException;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AllyRelationSubCommand extends SubCommand {

    public AllyRelationSubCommand() {
        super("ally", "relation.ally",
                "command.relation.ally.description", false);
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
            sendInvite(player, FactionHandler.getFaction(player), FactionHandler.getFaction(args[0]));
        } catch (PlayerHasNoFactionException e) {
            e.printStackTrace(); // This shouldn't happen, due to it being a requirement
        } catch (CantInviteYourselfException e) {
            Language.sendMessage("command.relation.ally.cant-invite-yourself", player);
        } catch (FactionOwnerIsOfflineException e) {
            Language.sendMessage("command.relation.ally.faction-owner-offline", player);
        } catch (FactionNotInStorage e) {
            Parser.run("command.relation.ally.faction-not-found")
                    .parse("{faction}", args[0])
                    .send(player);
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

    private void sendInvite(@NotNull Player player, @NotNull Faction<?> inviter, @NotNull Faction<?> invited)
            throws CantInviteYourselfException, FactionOwnerIsOfflineException {
        if (invited.getRegistry().equals(inviter.getRegistry())) throw new CantInviteYourselfException();
        if (player.)

        Player receiver = Bukkit.getPlayer(invited.getOwner());
        if (receiver == null) throw new FactionOwnerIsOfflineException(invited);

        //ToDo: Add faction option to don't accept invites
        InviteHandler.createInvite(player, receiver, new AllyInvite(player, inviter, invited));
    }
}
