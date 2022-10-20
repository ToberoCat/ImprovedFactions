package io.github.toberocat.improvedFactions.core.setting;

import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * A interface containing methods for setting handling for any object
 */
public interface Settings {

    /**
     * Sets the value of the specified setting to the specified value.
     *
     * @param setting The setting to set.
     * @param value   The value to set the setting to.
     * @param <T>     The data type of the setting
     */
    <T> void setSetting(@NotNull Setting<T> setting, T value);

    /**
     * Get the value set for a setting
     *
     * @param setting The setting you want to receive the value from
     * @param <T>     The data type you expect to receive
     * @return The set data
     * @throws ErrorParsingSettingException This exception gets thrown when the setting can't
     *                                      be read from a storage medium
     */
    @NotNull <T> T getSetting(@NotNull Setting<T> setting) throws ErrorParsingSettingException;

    /**
     * Get all settings that are registered within this setting holder
     *
     * @return The settings as stream
     */
    @NotNull Stream<Setting<?>> listSettings();
}
