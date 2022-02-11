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

public class BanSubCommand extends SubCommand {
    public BanSubCommand() {
        super("ban", "", false);
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

        faction.ban(banned);
        Language.sendRawMessage("You banned &e" + banned.getName(), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return null;

        return Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> !faction.getFactionMemberManager().getBanned().contains(x))
                .map(OfflinePlayer::getName).toList();
    }
}
