package io.github.toberocat.core.debug;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncCore;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Debugger {

    public static void log(String message) {
        AsyncCore.Run(() -> {
            if (MainIF.getConfigManager().getValue("general.debugMode"))
                MainIF.LogMessage(Level.INFO, "&7"+message);
        });
    }

    public static void logWarning(String message) {
        AsyncCore.Run(() -> {
            if (MainIF.getConfigManager().getValue("general.debugMode"))
                MainIF.LogMessage(Level.WARNING, "&7"+message);
        });
    }

    public static boolean hasPermission(Player player, String perm) {
        AsyncCore.Run(() -> {
            if (MainIF.getConfigManager().getValue("general.debugMode")) MainIF.LogMessage(Level.INFO,
                    "Permission check for &6" + player.getName() + "&7 with &6" + perm);
        });


        return player.hasPermission(perm);
    }
}
