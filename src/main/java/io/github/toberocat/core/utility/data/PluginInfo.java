package io.github.toberocat.core.utility.data;

import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.version.Version;

import java.net.MalformedURLException;
import java.net.URL;

public class PluginInfo {

    private static PluginInfo instance;

    private Version latestVersion;
    private Version latestExtensionRegistry;

    public static PluginInfo read() {
        if (instance == null) {
            try {
                instance = JsonUtility.readObjectFromURL(
                        new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/plugin-info.json"),
                        PluginInfo.class);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public String getLatestVersion() {
        return latestVersion.getVersion();
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = Version.from(latestVersion);
    }

    public String getLatestExtensionRegistry() {
        return latestExtensionRegistry.getVersion();
    }

    public void setLatestExtensionRegistry(String latestExtensionRegistry) {
        this.latestExtensionRegistry = Version.from(latestExtensionRegistry);
    }
}
