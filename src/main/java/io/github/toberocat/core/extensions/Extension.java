package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.utility.version.UpdateChecker;

import java.util.Arrays;
import java.util.logging.Level;

public abstract class Extension {

    protected ExtensionRegistry registry;
    protected boolean enabled = false;
    /**
     * Empty constructor needed, else not able to load .jar
     */
    public Extension() {
    }

    /**
     * Gets called to get informations like name, version, dependencies
     */
    protected abstract ExtensionRegistry register();

    public final void Enable(String filename, MainIF plugin) {
        registry = register();

        ExtensionListLoader.getExtensionVersion(filename).setFinishCallback((newestVersion) -> {
            UpdateChecker checker = new UpdateChecker(registry.version(), newestVersion);
            if (!checker.isNewestVersion()) {
                MainIF.LogMessage(Level.INFO, "&aHey! There is a newer version of "
                        + registry.displayName() + ". Use &7/f extension update&a to get the newest version");
            }
        });

        if (canEnable(plugin)) {
            OnEnable(plugin);
            if (Arrays.asList(registry.testedVersions()).contains(MainIF.getVersion()))
                MainIF.LogMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with tested " +
                        "version &6" + MainIF.getVersion());
            else
                MainIF.LogMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with version " +
                        "&6" + MainIF.getVersion() + "&a. &eThere may be problems with this extension");
            enabled = true;
        }
    }

    public boolean canEnable(MainIF plugin) {
        if (registry.dependencies() != null) {
            for (String depend : registry.dependencies()) {
                if (plugin.getServer().getPluginManager().getPlugin(depend) == null) {
                    MainIF.LogMessage(Level.WARNING, "&cDidn't find " + depend + ". "
                            + registry.displayName() + " requires " + Arrays.toString(registry.dependencies()));
                    return false;
                }
            }
        }
        if (registry.minVersion().versionToInteger() < MainIF.getVersion().versionToInteger()) {
            MainIF.LogMessage(Level.WARNING, "§c &6" + registry.displayName() + "v" + registry.version() + "&a " +
                    "needs a minimum version of &6" + registry.minVersion() + "&a. Currently on &6" + MainIF.getVersion());
            return false;
        }
        return true;
    }

    /**
     * This function is called when the extension is enabling.
     * This should add all the functionally needed in this extension
     * @param plugin the JavaPlugin
     */
    protected void OnEnable(MainIF plugin) {

    }

    public final void Disable(MainIF plugin) {
        OnDisable(plugin);
    }
    /**
     * This function is called when the extension is disabling.
     * This should remove all the functionally needed in this extension
     * @param plugin the JavaPlugin
     */
    protected void OnDisable(MainIF plugin) {

    }

    public ExtensionRegistry getRegistry() {
        return registry;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
