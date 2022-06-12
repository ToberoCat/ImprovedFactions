package io.github.toberocat.core.utility.color;

import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.type.SettingEnum;

public enum FactionColors implements SettingEnum {
    WHITE("White", "#ffffff"), DARK_BLUE("Dark blue", "#0000B9"),
    DARK_GREEN("Dark green", "#013220"), DARK_AQUA("Dark aqua", "#007272"),
    DARK_RED("Dark red", "#720000"), DARK_PURPLE("Dark purple", "#4d004d"),
    GOLD("Gold", "#ffae1a"), GRAY("Gray", "#a6a6a6"),
    DARK_GRAY("Dark gray", "#444444"), BLUE("Blue", "#3333ff"),
    GREEN("Green", "#00cd00"), AQUA("Aqua", "#00ffea"),
    RED("Red", "#ff0033"), LIGHT_PURPLE("Light purple", "#cd00cd"),
    YELLOW("Yellow", "#ffff1a"), BLACK("§7Black", "#1a1a1a");

    final String display;
    final int color;

    FactionColors(String display, String hexColor) {
        this.display = Language.format(hexColor + display);
        this.color = parseColor(hexColor);
    }

    public static int parseColor(String colorString) {
        if (colorString.charAt(0) == '#') return -1;
        return (int) Long.parseLong(colorString.substring(1), 16);
    }

    public int getColor() {
        return color;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
