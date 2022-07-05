package io.github.toberocat.improvedfactions.extentions;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.UpdateChecker;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionListLoader;
import io.github.toberocat.improvedfactions.utility.Debugger;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public abstract class Extension {

    protected ExtensionRegistry registry;
    protected UpdateChecker updateChecker;
    protected boolean enabled = false;
    /**
     * Empty constructor needed, else not able to load .jar
     */
    public Extension() {
    }

    protected abstract ExtensionRegistry register();

    public final void Enable(ImprovedFactionsMain plugin) {
        registry = register();
        ExtensionObject object = ExtensionListLoader.getExtenionObject(registry.getName());

        if (object != null) {
            updateChecker = new UpdateChecker(registry.getVersion(), object.getVersion());


            if (!updateChecker.isNewestVersion())
                plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §fGood news. There is a newer version of §e§l" + registry.getName());

            if (object.getDependencies() != null) {
                boolean hasAllDepends = true;
                for (String dependency : object.getDependencies()) {
                    if (!hasDependency(dependency, plugin)) {
                        plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §cDidn't find " + dependency + ". " + registry.getName() + " requires " + Arrays.toString(object.getDependencies()));
                        hasAllDepends = false;
                        return;
                    }
                }
                if (!hasAllDepends) return;
            }
        }


        plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §eLoading " + registry.getName() + " v" + registry.getVersion());
        OnEnable(plugin);
        enabled = true;

        /*
        if (Arrays.asList(registry.getPluginVersions()).contains(ImprovedFactionsMain.getVERSION())) {
            plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §eLoading " + registry.getName() + " v" + registry.getVersion());
            OnEnable(plugin);
            enabled = true;
        } else {
            plugin.getServer().getConsoleSender().sendMessage("§7[Factions] §c " + registry.getName() + " v" + registry.getVersion() + " needs plugin versions " + Arrays.toString(registry.getPluginVersions()));
        }
         */

    }

    private static boolean hasDependency(String dependency, ImprovedFactionsMain plugin) {
        for (Plugin plg : plugin.getServer().getPluginManager().getPlugins()) {
            if (plg.getName().equals(dependency)){
                return true;
            }
        }
        return false;
    }

    /**
     * This function is called when the extension is enabling.
     * This should add all the functionally needed in this extension
     * @param plugin the JavaPlugin
     */
    protected void OnEnable(ImprovedFactionsMain plugin) {

    }

    public boolean preLoad(ImprovedFactionsMain plugin) {
        return true;
    }

    public final void Disable(ImprovedFactionsMain plugin) {
        OnDisable(plugin);
    }
    /**
     * This function is called when the extension is disabling.
     * This should remove all the functionally needed in this extension
     * @param plugin the JavaPlugin
     */
    protected void OnDisable(ImprovedFactionsMain plugin) {

    }

    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public ExtensionRegistry getRegistry() {
        return register();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
