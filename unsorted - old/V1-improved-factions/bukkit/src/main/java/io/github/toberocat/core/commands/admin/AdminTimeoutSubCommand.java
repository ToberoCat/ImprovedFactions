package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.player.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.List;

public class AdminTimeoutSubCommand extends SubCommand {
    public AdminTimeoutSubCommand() {
        super("timeout", "admin.timeout", "command.admin.timeout.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(2);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        if (offlinePlayer == null) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        double timeout = Float.parseFloat(args[1]) * 60 * 1000 + System.currentTimeMillis();

        PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").setSelected(timeout);
        LocalDate date = new LocalDate(timeout);
        DateTimeFormatter time = DateTimeFormat.forPattern("mm:hh dd.MM");

        Language.sendRawMessage("You have sent &6" + offlinePlayer.getName() + "&f into timeout until &6" +
                date.toString(time), player);

        if (!offlinePlayer.isOnline()) return;

        Language.sendRawMessage("You have been sent to timeout until &6" + date.toString(time) + "&f. " +
                "You are not able to join any factions as long as you are in timeout", offlinePlayer.getPlayer());
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        if (args.length <= 1) {
            return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
        }
        return Arrays.asList("<Minutes>", "1", "3", "15", "60");
    }
}
