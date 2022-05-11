package io.github.toberocat.core.utility.color;

import io.github.toberocat.core.utility.settings.type.SettingEnum;
import org.bukkit.ChatColor;

public enum FactionColors implements SettingEnum {
    WHITE("White"), DARK_BLUE("Dark blue"), DARK_GREEN("Dark green"),
    DARK_AQUA("Dark aqua"), DARK_RED("Dark red"), DARK_PURPLE("Dark purple"),
    GOLD("Gold"), GRAY("Gray"), DARK_GRAY("Dark gray"),
    BLUE("Blue"), GREEN("Green"), AQUA("Aqua"), RED("Red"),
    LIGHT_PURPLE("Light purple"), YELLOW("Yellow"), BLACK("§7Black");

    String display;
    FactionColors(String display) {
        this.display = ChatColor.valueOf(name()) + display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
