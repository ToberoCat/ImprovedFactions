package io.github.toberocat.improvedfactions.commands.factionCommands.adminSubCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class ByPassSubCommand extends SubCommand {

    public static List<UUID> BYPASS = new ArrayList<>();

    public ByPassSubCommand() {
        super("bypass", "");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        UUID toggle;
        Player message = null;
        if (args.length == 0) {
            toggle = player.getUniqueId();
            message = player;
        } else {
            OfflinePlayer user = Bukkit.getOfflinePlayer(args[0]);
            if (user == null) {
                CommandExecuteError(CommandExecuteError.PlayerNotFound, player);
                return;
            }

            toggle = user.getUniqueId();
            if (user.isOnline()) message = user.getPlayer();
        }

        if (BYPASS.contains(toggle)) {
            BYPASS.remove(toggle);
            if (message == null) Language.sendRawMessage("Disabled bypass for " + args[0], player);
            else Language.sendRawMessage("Disabled bypass", message);
        }
        else {
            BYPASS.add(toggle);
            if (message == null) Language.sendRawMessage("Enabled bypass for " + args[0], player);
            else Language.sendRawMessage("Enabled bypass", message);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
    }
}
