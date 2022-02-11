package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class UnBanSubCommand extends SubCommand {
    public UnBanSubCommand() {
        super("unban", "", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        OfflinePlayer banned = Bukkit.getOfflinePlayer(args[0]);

        if (banned == null) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        faction.unban(banned);
        Language.sendRawMessage("You unbanned &e" + banned.getName(), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return null;

        return faction.getFactionMemberManager().getBanned().stream().map(x -> Bukkit.getOfflinePlayer(x).getName()).toList();
    }
}
