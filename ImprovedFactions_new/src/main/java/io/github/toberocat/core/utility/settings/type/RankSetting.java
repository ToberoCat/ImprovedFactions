package io.github.toberocat.core.utility.settings.type;

import io.github.toberocat.core.factions.rank.Rank;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class RankSetting extends Setting<String[]> {

    public RankSetting() {
    }

    public RankSetting(String settingName, String[] allowedRanks, ItemStack display) {
        super(settingName, allowedRanks, display);
    }

    public boolean hasPermission(Rank rank) {
        return Arrays.stream(selected).anyMatch(x -> x.equals(rank.getRegistryName()));
    }
}
