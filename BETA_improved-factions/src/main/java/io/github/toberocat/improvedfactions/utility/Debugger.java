package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.language.Language;

import java.util.logging.Level;

public class Debugger {

    public static void LogInfo(String message) {
        if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.debugMode")) {
            IgnoreSettingsInfo(message);
        }
    }

    public static void LogSevere(String message) {
        if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.debugMode")) {
            IgnoreSettingsSevere(message);
        }
    }

    public static void LogWarning(String message) {
        if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.debugMode")) {
            IgnoreSettingsWarning(message);
        }
    }

    public static void IgnoreSettingsInfo(String message) {
        ImprovedFactionsMain.getPlugin().getLogger().log(Level.INFO, Language.format(message));
    }

    public static void IgnoreSettingsSevere(String message) {
        ImprovedFactionsMain.getPlugin().getLogger().log(Level.SEVERE, Language.format(message));
    }

    public static void IgnoreSettingsWarning(String message) {
        ImprovedFactionsMain.getPlugin().getLogger().log(Level.WARNING, Language.format(message));
    }
}
