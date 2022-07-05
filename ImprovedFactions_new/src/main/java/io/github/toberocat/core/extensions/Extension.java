package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.version.UpdateChecker;
import io.github.toberocat.core.utility.version.Version;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public abstract class Extension {

    protected ExtensionRegistry registry = null;
    protected boolean enabled = false;

    /**
     * Empty constructor needed, else not able to load .jar
     */
    public Extension() {
    }

    public final <T> T configValue(String section) {
        if (registry == null) {
            MainIF.logMessage(Level.WARNING, "Cannot read value at section " + section + " while registry hasn't set (yet)");
            return null;
        }

        return MainIF.getConfigManager().getValue(registry.registry() + "." + section);
    }

    public final <T> void setConfigDefaultValue(String section, T value) {
        if (registry == null) {
            MainIF.logMessage(Level.WARNING, "Cannot register value at section " + section + " while registry hasn't set (yet)");
            return;
        }
        MainIF.getConfigManager().addToDefaultConfig(registry.registry() + "." + section, value);
    }


    /**
     * Gets called to get informations like name, version, dependencies
     */
    public final void enable(@NotNull ExtensionRegistry registry, MainIF plugin) {
        if (this.registry != null) return;
        this.registry = registry;

        if (!canEnable(plugin)) return;

        onEnable(plugin);
        if (Arrays.stream(registry.testedVersions()).map(Version::getVersion).anyMatch(x -> x.equals(MainIF.getVersion().getVersion())))
            MainIF.logMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with tested " +
                    "version &6" + MainIF.getVersion());
        else
            MainIF.logMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with version " +
                    "&6" + MainIF.getVersion() + "&a. &eThis version could have complications with the extension");

        AsyncTask.runLater(0, this::updateExtension);
        enabled = true;
    }

    private void updateExtension() {
        ExtensionListLoader.getMap().then((map) -> {
            if (latestVersion(map)) return;

            ExtensionDownloader.downloadExtension(map.get(registry.registry()), new ExtensionDownloadCallback() {
                @Override
                public void startDownload(ExtensionObject extension) {
                    MainIF.logMessage(Level.INFO, "&a&lStarted&f extension update for &e" + registry.displayName() +
                            "&a. Don't restart the server!");
                }

                @Override
                public void cancelDownload(ExtensionObject extension) {
                    MainIF.logMessage(Level.WARNING, "&c&lSomething&f went wrong while updating &e" + registry.displayName() +
                            "&a. File could be corrupted");
                }

                @Override
                public void finishedDownload(ExtensionObject extension) {
                    MainIF.logMessage(Level.INFO, "&a&lInstalled&f extension update for &e" + registry.displayName() +
                            "&a. Reloading the server now");
                    Bukkit.reload();
                }
            });
        });
    }

    private boolean latestVersion(HashMap<String, ExtensionObject> map) {
        if (!map.containsKey(registry.registry())) return true;

        Version newest = map.get(registry.registry()).getNewestVersion();
        Version current = registry.version();

        return new UpdateChecker(current, newest).isNewestVersion();
    }

    public boolean canEnable(MainIF plugin) {
        if (registry.dependencies() != null) {
            for (String depend : registry.dependencies()) {
                if (plugin.getServer().getPluginManager().getPlugin(depend) == null) {
                    MainIF.logMessage(Level.WARNING, "&cDidn't find " + depend + ". "
                            + registry.displayName() + " requires " + Arrays.toString(registry.dependencies()));
                    return false;
                }
            }
        }
        if (registry.extensionDependencies() != null) {
            for (String extension : registry.extensionDependencies()) {
                if (!MainIF.LOADED_EXTENSIONS.containsKey(extension)) {
                    MainIF.logMessage(Level.WARNING, "&c" + registry.displayName() +
                            " requires the extension " + extension + " to work");
                    return false;
                }
            }
        }
        if (registry.minVersion().versionToInteger() > MainIF.getVersion().versionToInteger()) {
            MainIF.logMessage(Level.WARNING, "&6" + registry.displayName() + "&c with version &6" + registry.version() + "&c " +
                    "needs a minimum version of &6" + registry.minVersion() + "&c. You are currently on &6" + MainIF.getVersion());
            return false;
        }
        return true;
    }

    /**
     * This function is called when the extension is enabling.
     * This should add all the functionally needed in this extension
     *
     * @param plugin the JavaPlugin
     */
    protected void onEnable(MainIF plugin) {

    }

    public final void disable(MainIF plugin) {
        onDisable(plugin);
    }

    /**
     * This function is called when the extension is disabling.
     * This should remove all the functionally needed in this extension
     *
     * @param plugin the JavaPlugin
     */
    protected void onDisable(MainIF plugin) {

    }

    public void reloadConfigs() {

    }

    public ExtensionRegistry getRegistry() {
        return registry;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
