package io.github.toberocat.core.utility.color;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.type.SettingEnum;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum FactionColors implements SettingEnum {
    WHITE("White", 0xffffff), DARK_BLUE("Dark blue", 0x0000B9),
    DARK_GREEN("Dark green", 0x013220), DARK_AQUA("Dark aqua", 0x007272),
    DARK_RED("Dark red", 0x720000), DARK_PURPLE("Dark purple", 0x4d004d),
    GOLD("Gold", 0xffae1a), GRAY("Gray", 0xa6a6a6),
    DARK_GRAY("Dark gray", 0x444444), BLUE("Blue", 0x3333ff),
    GREEN("Green", 0x00cd00), AQUA("Aqua", 0x00ffea),
    RED("Red", 0xff0033), LIGHT_PURPLE("Light purple", 0xcd00cd),
    YELLOW("Yellow", 0xffff1a), BLACK("§7Black", 0x1a1a1a);

    final String display;
    final int color;

    FactionColors(String display, int color) {
        this.display = Language.format("#" + Integer.toHexString(color) + display);
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
