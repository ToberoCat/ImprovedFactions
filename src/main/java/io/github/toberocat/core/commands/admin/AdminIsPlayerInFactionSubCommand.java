package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminIsPlayerInFactionSubCommand extends SubCommand {
    public AdminIsPlayerInFactionSubCommand() {
        super("isinfaction", "", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Player onP = Bukkit.getPlayer(args[0]);

        if (onP == null || !onP.isOnline()) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        boolean inFaction = FactionUtility.isInFaction(player);
        Language.sendRawMessage("&6" + args[0] + "&f is in " + (inFaction ? "a faction" : "no faction"), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
    }
}
