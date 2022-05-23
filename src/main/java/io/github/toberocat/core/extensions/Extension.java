package io.github.toberocat.core.extensions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.utility.version.UpdateChecker;
import io.github.toberocat.core.utility.version.Version;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public abstract class Extension {

    protected ExtensionRegistry registry = null;
    protected boolean enabled = false;

    /**
     * Empty constructor needed, else not able to load .jar
     */
    public Extension() {}

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

        if (canEnable(plugin)) {
            onEnable(plugin);
            if (Arrays.stream(registry.testedVersions()).map(Version::getVersion).anyMatch(x -> x.equals(MainIF.getVersion().getVersion())))
                MainIF.logMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with tested " +
                        "version &6" + MainIF.getVersion());
            else
                MainIF.logMessage(Level.INFO, "&aLoading &6" + registry.displayName() + "&a with version " +
                        "&6" + MainIF.getVersion() + "&a. &eThis version could have complications with the extension");
            enabled = true;
        }
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
