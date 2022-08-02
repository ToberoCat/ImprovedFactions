package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.factions.local.managers.FactionMemberManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteSubCommand extends SubCommand {
    public InviteSubCommand() {
        super("invite", "command.invite.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes).setArgLength(1);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Faction<?> faction = FactionHandler.getFaction(player);
        if (faction == null) sendCommandExecuteError(CommandExecuteError.NoFaction, player);

        Player invited = Bukkit.getPlayer(args[0]);

        if (invited == null) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        if (DateCore.hasTimeout(player))
            Language.sendMessage("command.invite.player-has-timeout", player);

        faction.getFactionMemberManager().invitePlayer(invited);
        Language.sendRawMessage("You invited " + args[0] + ". Invitation will be rejected after 5 minutes", player);

    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        FactionMemberManager manager = FactionManager.getPlayerFaction(player).getFactionMemberManager();
        return Bukkit.getOnlinePlayers().stream().filter(user -> !manager.getMembers().contains(user.getUniqueId())
                && !manager.getBanned().contains(user.getUniqueId())).map(Player::getName).toList();
    }
}
