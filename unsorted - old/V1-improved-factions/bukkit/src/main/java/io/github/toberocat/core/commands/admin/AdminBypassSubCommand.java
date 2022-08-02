package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AdminBypassSubCommand extends SubCommand {
    public static final List<UUID> BYPASSING = new ArrayList<>();

    public AdminBypassSubCommand() {
        super("bypass", "admin.bypass", "command.admin.bypass.description", false);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        OfflinePlayer selected = null;
        if (args.length == 0) {
            selected = player;
        } else {
            selected = Bukkit.getOfflinePlayer(args[0]);
        }

        if (selected == null) {
            sendCommandExecuteError(CommandExecuteError.PlayerNotFound, player);
            return;
        }

        if (BYPASSING.contains(selected.getUniqueId())) {
            BYPASSING.remove(selected.getUniqueId());
            if (selected.isOnline()) {
                Language.sendRawMessage("You can no longer bypass claims", selected.getPlayer());
            }

            if (selected.getUniqueId() != player.getUniqueId()) {
                Language.sendRawMessage(selected.getName() + " can no longer bypass claims", player);
            }
        } else {
            BYPASSING.add(selected.getUniqueId());
            if (selected.isOnline()) {
                Language.sendRawMessage("You can now bypass claims", selected.getPlayer());
            }

            if (selected.getUniqueId() != player.getUniqueId()) {
                Language.sendRawMessage(selected.getName() + " is now able to bypass claims", player);
            }
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
    }
}
