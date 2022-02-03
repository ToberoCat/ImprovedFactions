package io.github.toberocat.versions.nms;

import io.github.toberocat.MainIF;
import org.bukkit.Bukkit;

import java.util.logging.Level;

/**
 * This is the interface for loading nms stuff
 */
public interface NMSInterface {
    /**
     * This will send a message by default, telling the user what version he currently is using of this plugin
     */
    default void EnableInterface() {
        MainIF.LogMessage(Level.INFO, "&aDetected version &6" + Bukkit.getBukkitVersion() +
                "&a running on server. Loaded IF version support &6" + getVersion());
    }

    /**
     * Define what version this interface should get loaded to
     * @return The version string. E.g: "1.17.x"
     */
    String getVersion();

    /**
     * This will get called when the version got detected.
     * Do your nms stuff in here
     */
    void Load();
}
