package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteAccept extends SubCommand {
    public InviteAccept() {
        super("inviteaccept", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }

        if (FactionUtils.getFactionByRegistry(args[0]) == null) {
            player.sendMessage(Language.getPrefix() + "§cCoudln't find faction searching for");
            return;
        }

        JoinPrivateFactionSubCommand.JoinPrivate(player, args[0]);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return ImprovedFactionsMain.playerData.get(player.getUniqueId()).invitations;
    }
}
