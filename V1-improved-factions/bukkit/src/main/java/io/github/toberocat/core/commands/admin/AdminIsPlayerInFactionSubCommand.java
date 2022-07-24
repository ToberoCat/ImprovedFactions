package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminIsPlayerInFactionSubCommand extends SubCommand {
    public AdminIsPlayerInFactionSubCommand() {
        super("isinfaction", "admin.isinfaction", "command.admin.if-player-faction.description", false);
    }


    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Player onP = Bukkit.getPlayer(args[0]);

        if (onP == null || !onP.isOnline()) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        boolean inFaction = FactionManager.isInFaction(player);
        Language.sendRawMessage("&6" + args[0] + "&f is in " + (inFaction ? "a faction" : "no faction"), player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
    }
}
