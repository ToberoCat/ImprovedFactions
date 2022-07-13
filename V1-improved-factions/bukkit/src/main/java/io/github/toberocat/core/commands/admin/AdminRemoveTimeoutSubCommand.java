package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.player.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminRemoveTimeoutSubCommand extends SubCommand {
    public AdminRemoveTimeoutSubCommand() {
        super("deltimeout", "admin.deltimeout", "command.admin.deltimeout.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (offlinePlayer == null) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").setSelected(-1);

        Language.sendRawMessage("You have removed &6" + offlinePlayer.getName() + "&f's  timeout", player);

        if (!offlinePlayer.isOnline()) return;

        Language.sendRawMessage("You are no longer in timeout", offlinePlayer.getPlayer());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
    }
}
