package io.github.toberocat.core.utility.settings.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;

import static io.github.toberocat.core.utility.language.Language.format;

public class EnumSetting extends Setting<Integer> {

    public String[] values;
    private BiConsumer<Player, Integer> update;

    public EnumSetting() {
    }

    public <T extends SettingEnum> EnumSetting(T[] enumValues, String settingName, ItemStack display) {
        super(settingName, 0, display);
        this.values = Arrays.stream(enumValues).map(value -> format(value.getDisplay())).toArray(String[]::new);
        loadDefaultFromConfig();
    }

    public EnumSetting(String[] values, String settingName, ItemStack display) {
        super(settingName, 0, display);
        this.values = values;
        loadDefaultFromConfig();
    }

    private void loadDefaultFromConfig() {
        config.getConfig().addDefault("settings." + settingName + ".defaulted", values[0]);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        String val = config.getConfig().getString("settings." + settingName + ".defaulted");
        int index = ArrayUtils.indexOf(values, val);
        if (index == -1) {
            MainIF.logMessage(Level.WARNING, "Enum value for setting " + settingName +
                    " couldn't get read from config, due to not existing enum type. Value is "
                    + val + ". Only these options are available: "
                    + Arrays.toString(values) + ". Processing with using the default value");
        }

        selected = index;
    }

    @Override
    public void fromString(@NotNull String value) {
        this.selected = Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return String.valueOf(selected);
    }

    public void rotateSelection(Player player) {
        selected++;

        if (selected >= values.length) {
            selected = 0;
        }

        if (update != null) update.accept(player, selected);
    }

    @JsonIgnore
    public void setUpdate(BiConsumer<Player, Integer> update) {
        this.update = update;
    }


    @JsonIgnore
    public String[] getValues() {
        return values;
    }

    @JsonIgnore
    public void setValues(String[] values) {
        this.values = values;
    }
}
