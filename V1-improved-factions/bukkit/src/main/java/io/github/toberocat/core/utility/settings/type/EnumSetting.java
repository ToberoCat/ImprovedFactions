package io.github.toberocat.core.utility.settings.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.github.toberocat.core.utility.language.Language.format;

public class EnumSetting extends Setting<Integer> {

    public String[] values;
    private BiConsumer<Player, Integer> update;

    public EnumSetting() {
    }

    public <T extends SettingEnum> EnumSetting(T[] enumValues, String settingName, ItemStack display) {
        super(settingName, 0, display);
        this.values = Arrays.stream(enumValues).map(value -> format(value.getDisplay())).toArray(String[]::new);
    }

    public EnumSetting(String[] values, String settingName, ItemStack display) {
        super(settingName, 0, display);
        this.values = values;
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
