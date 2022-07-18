package io.github.toberocat.core.utility.settings.type;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BoolSetting extends Setting<Boolean> {
    public BoolSetting(String settingName, boolean selected, ItemStack display) {
        super(settingName, selected, display);

        config.getConfig().addDefault("settings." + settingName + ".defaulted", selected);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        this.selected = config.getConfig().getBoolean("settings." + settingName + ".defaulted");
    }

    @Override
    public void fromString(@NotNull String value) {
        selected = Boolean.valueOf(value);
    }

    @Override
    public String toString() {
        return String.valueOf(selected);
    }
}
