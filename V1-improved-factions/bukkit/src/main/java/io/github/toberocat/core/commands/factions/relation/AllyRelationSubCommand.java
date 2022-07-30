package io.github.toberocat.core.commands.factions.relation;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.invite.components.AllyInvite;
import io.github.toberocat.core.invite.handler.InviteHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.FactionOwnerIsOfflineException;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.exceptions.faction.relation.AlreadyInvitedException;
import io.github.toberocat.core.utility.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
            FactionHandler.getFaction(player).inviteAlly(FactionHandler.getFaction(args[0]));
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
        } catch (FactionIsFrozenException e) {
            e.printStackTrace();
        } catch (AlreadyInvitedException e) {
            e.printStackTrace();
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
