package io.github.toberocat.core.utility.settings.type;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.components.rank.Rank;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONValue;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class RankSetting extends Setting<String[]> {

    public RankSetting() {
    }

    public RankSetting(String settingName, String[] allowedRanks, ItemStack display) {
        super(settingName, allowedRanks, display);
        loadDefaultFromConfig();
    }

    private void loadDefaultFromConfig() {
        config.getConfig().addDefault("settings." + settingName + ".defaulted",
                selected);
        config.getConfig().options().copyDefaults(true);
        config.saveConfig();

        List<String> val = config.getConfig().getStringList("settings." + settingName + ".defaulted");
        selected = val.toArray(String[]::new);
    }


    @Override
    public void fromString(@NotNull String value) {
        selected = Arrays.stream(
                value.replaceAll("[\\[]]","")
                .split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    @Override
    public String toString() {
        return Arrays.toString(selected);
    }

    public boolean hasPermission(Rank rank) {
        return Arrays.stream(selected).anyMatch(x -> x.equals(rank.getRegistryName()));
    }
}
