package io.github.toberocat.core.player;

import io.github.toberocat.core.utility.settings.type.SettingEnum;

public enum TitlePosition implements SettingEnum {
    TITLE("Title"), SUBTITLE("Subtitle"),
    ACTIONBAR("Actionbar"), CHAT("Chat"),
    HIDDEN("Hidden");

    String display;

    TitlePosition(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}

