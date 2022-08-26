package io.github.toberocat.improvedFactions.core.setting;

import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface SettingHolder {
    <T> void setSetting(@NotNull Setting<T> setting, T value);

    @NotNull <T> T getSetting(@NotNull Setting<T> setting) throws ErrorParsingSettingException;

    @NotNull Stream<Setting<?>> listSettings();
}
