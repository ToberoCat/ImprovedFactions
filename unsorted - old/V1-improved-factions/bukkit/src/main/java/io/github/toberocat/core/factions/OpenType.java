package io.github.toberocat.core.factions;

import io.github.toberocat.core.utility.settings.type.SettingEnum;

public enum OpenType implements SettingEnum {
    PUBLIC("Public"), INVITE_ONLY("Invite only"), CLOSED("Closed");

    String display;

    OpenType(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
