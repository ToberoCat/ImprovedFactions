package io.github.toberocat.core.debug;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class Debugger {

    public static void log(String message) {
        AsyncTask.run(() -> {
            if (MainIF.config().getBoolean("general.debugMode", false))
                MainIF.logMessage(Level.INFO, "&7" + message);
        });
    }

    public static void logWarning(String message) {
        AsyncTask.run(() -> {
            if (MainIF.config().getBoolean("general.debugMode", false))
                MainIF.logMessage(Level.WARNING, "&7" + message);
        });
    }

    public static boolean hasPermission(Player player, String perm) {
        AsyncTask.run(() -> {
            if (MainIF.config().getBoolean("general.debugMode", false))
                MainIF.logMessage(Level.INFO,
                    "Permission check for &6" + player.getName() + "&7 with &6" + perm);
        });


        return player.hasPermission(perm);
    }
}
