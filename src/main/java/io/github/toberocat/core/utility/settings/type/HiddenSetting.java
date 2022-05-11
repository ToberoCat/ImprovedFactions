package io.github.toberocat.core.utility.settings.type;

public class HiddenSetting<T> extends Setting<T> {

    public HiddenSetting() {
    }

    public HiddenSetting(String settingName, T t) {
        super(settingName, t, null);
    }
}
