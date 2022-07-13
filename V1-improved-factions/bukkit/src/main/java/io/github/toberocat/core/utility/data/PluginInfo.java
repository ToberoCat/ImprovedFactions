package io.github.toberocat.core.utility.data;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.version.Version;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

public class PluginInfo {

    private static PluginInfo instance;

    private Version latestVersion;
    private Version latestExtensionRegistry;

    public static AsyncTask<PluginInfo> fetch() {
        if (instance == null) return AsyncTask.run(() -> {
                try {
                    instance = JsonUtility.readObjectFromURL(
                            new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/plugin-info.json"),
                            PluginInfo.class);
                } catch (IOException ignored) {
                    MainIF.logMessage(Level.WARNING, "Couldn't reach github to download latest version files");
                    instance = new PluginInfo(MainIF.getVersion(), Version.from("0.0"));
                }
                return instance;
            });
        return AsyncTask.returnItem(instance);
    }

    public PluginInfo() {
    }

    public PluginInfo(Version latestVersion, Version latestExtensionRegistry) {
        this.latestVersion = latestVersion;
        this.latestExtensionRegistry = latestExtensionRegistry;
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
