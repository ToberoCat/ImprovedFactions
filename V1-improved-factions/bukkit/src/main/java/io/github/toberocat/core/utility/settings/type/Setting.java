package io.github.toberocat.core.utility.settings.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.callbacks.Callback;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.gui.slot.Slot;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class Setting<T> {
    protected static DataManager config = new DataManager(MainIF.getIF(), "settings.yml");

    protected T selected;
    protected ItemStack display;
    protected boolean isLocked;
    protected String settingName;

    public Setting() {
    }

    public Setting(String settingName, T t, ItemStack display) {
        this.selected = t;
        this.display = display;
        this.settingName = settingName;

        config.getConfig().addDefault("settings." + settingName + ".isLocked", false);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        this.isLocked = config.getConfig().getBoolean("settings." + settingName + ".isLocked");
    }

    public Setting(String settingName, T t, boolean isLocked, ItemStack display) {
        this.selected = t;
        this.display = display;
        this.settingName = settingName;

        config.getConfig().addDefault("settings." + settingName + ".isLocked", isLocked);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        this.isLocked = config.getConfig().getBoolean("settings." + settingName + ".isLocked");
    }

    public abstract void fromString(@NotNull String value);

    public static Map<String, Setting> populateSettings(Map<String, Setting> defaulted, Map<String, Setting> current) {
        if (current == null) current = defaulted;

        for (String key : defaulted.keySet()) {
            Setting defaultSettings = defaulted.get(key);
            Setting set = current.get(key);
            if (set == null) {
                throw new IllegalArgumentException("Setting was't found - " + key);
            }
            Object selected = set.getSelected();
            current.replace(key, defaultSettings);
            current.get(key).setSelected(selected);

            current.get(key).setSettingName(defaultSettings.getSettingName());
            current.get(key).setDisplay(defaultSettings.getDisplay());
            current.get(key).setLocked(defaultSettings.isLocked());

            if (defaultSettings instanceof EnumSetting enumDefaults) {
                ((EnumSetting) current.get(key)).setValues(enumDefaults.getValues());
            } else if (defaultSettings instanceof CallbackSettings cbSettings) {
                ((CallbackSettings) current.get(key)).setType(cbSettings.getType());
                ((CallbackSettings) current.get(key)).setCallback(cbSettings.getCallback());
            }
        }

        return current;
    }

    public static Slot getSlot(Setting setting, Player player, Callback render) {
        if (setting instanceof HiddenSetting) return null;

        if (setting instanceof BoolSetting boolSetting) {
            StringBuilder enabled = new StringBuilder("&a");
            StringBuilder disabled = new StringBuilder("&c");

            if (boolSetting.getSelected()) enabled.append("&n");
            else disabled.append("&n");

            List<String> defaultLore = Utility.getLore(boolSetting.getDisplay());
            defaultLore.add(0, "&7Type: &eBoolean");
            if (defaultLore.size() > 1) {
                defaultLore.add(1, "");
                defaultLore.add("");
            }
            defaultLore.addAll(List.of(enabled + "enabled", disabled + "disabled"));
            defaultLore.add("");
            if (!setting.isLocked) defaultLore.add("§8Click to toggle selected");
            else defaultLore.add("§6Can't interact. Setting is locked");

            if (setting.isLocked && Debugger.hasPermission(player, "factions.gui.unlock-settings")) {
                defaultLore.add("");
                defaultLore.add("&6You can interact with locked settings.");
                defaultLore.add("&6That's true power");
            }

            return new Slot(Utility.setLore(boolSetting.getDisplay(), defaultLore.toArray(String[]::new))) {
                @Override
                public void click(Player user) {
                    if (setting.isLocked && !Debugger.hasPermission(player, "factions.gui.unlock-settings")) return;

                    setting.setSelected(!(Boolean) setting.getSelected());
                    render.callback();
                }
            };
        } else if (setting instanceof EnumSetting enumSetting) {
            String[] values = enumSetting.getValues();
            List<String> lore = Utility.getLore(enumSetting.getDisplay());
            lore.add(0, "&7Type: &eSelector");

            if (lore.size() > 1) {
                lore.add(1, "");
                lore.add("");
            }

            for (int i = 0; i < values.length; i++) {
                lore.add((enumSetting.getSelected() == i ? "&f&n" + ChatColor.stripColor(values[i]) :
                        "&7" + values[i]));
            }
            lore.add("");
            if (!setting.isLocked) lore.add("§8Click to switch selected");
            else lore.add("§6Can't interact. Setting is locked");

            if (setting.isLocked && Debugger.hasPermission(player, "factions.gui.unlock-settings")) {
                lore.add("");
                lore.add("&6You can interact with locked settings.");
                lore.add("&6That's true power");
            }

            return new Slot(Utility.setLore(enumSetting.getDisplay(), lore.toArray(String[]::new))) {
                @Override
                public void click(Player user) {
                    if (setting.isLocked && !Debugger.hasPermission(player, "factions.gui.unlock-settings")) return;

                    enumSetting.rotateSelection(player);
                    render.callback();
                }
            };
        } else if (setting instanceof CallbackSettings callbackSettings) {
            List<String> lore = Utility.getLore(callbackSettings.getDisplay());
            lore.add(0, "&7Type: &e" + callbackSettings.getType());

            if (lore.size() > 1) {
                lore.add(1, "");
                lore.add("");
            }

            if (!setting.isLocked) lore.add("§8Click to execute");
            else lore.add("§6Can't interact. Setting is locked");

            if (setting.isLocked && Debugger.hasPermission(player, "factions.gui.unlock-settings")) {
                lore.add("");
                lore.add("&6You can interact with locked settings.");
                lore.add("&6That's true power");
            }

            return new Slot(Utility.setLore(callbackSettings.getDisplay(), lore.toArray(String[]::new))) {
                @Override
                public void click(Player user) {
                    if (setting.isLocked && !Debugger.hasPermission(player, "factions.gui.unlock-settings")) return;

                    AsyncTask.runLaterSync(0, () -> callbackSettings.execute(user));
                }
            };
        }

        return new Slot(setting.getDisplay()) {
            @Override
            public void click(Player user) {
                if (setting.isLocked && !Debugger.hasPermission(player, "factions.gui.unlock-settings")) return;

                render.callback();
            }
        };
    }

    public T getSelected() {
        return selected;
    }

    public void setSelected(T selected) {
        this.selected = selected;
    }

    @JsonIgnore
    public ItemStack getDisplay() {
        return display;
    }

    @JsonIgnore
    public void setDisplay(ItemStack display) {
        this.display = display;
    }

    @JsonIgnore
    public boolean isLocked() {
        return isLocked;
    }

    @JsonIgnore
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @JsonIgnore
    public String getSettingName() {
        return settingName;
    }

    @JsonIgnore
    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }
}
