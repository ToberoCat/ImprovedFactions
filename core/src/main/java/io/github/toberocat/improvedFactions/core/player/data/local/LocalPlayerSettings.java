package io.github.toberocat.improvedFactions.core.player.data.local;

import io.github.toberocat.improvedFactions.core.exceptions.setting.ErrorParsingSettingException;
import io.github.toberocat.improvedFactions.core.setting.Setting;
import io.github.toberocat.improvedFactions.core.setting.SettingHolder;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

public class LocalPlayerSettings implements SettingHolder {

    private final HashMap<String, String> settingValues;
    private final FileAccess access;
    private final UUID id;


    public LocalPlayerSettings(@NotNull FileAccess access, @NotNull UUID id) {
        this.id = id;
        this.access = access;
        settingValues = loadFromFile().settingValues();
    }

    private @NotNull MapWrapper loadFromFile() {
        try {
            return access.read(MapWrapper.class, id + ".json");
        } catch (IOException e) {
            return new MapWrapper(new HashMap<>());
        }

    }

    @Override
    public <T> void setSetting(@NotNull Setting<T> setting, T value) {
        settingValues.put(setting.label(), setting.toSave(value));
    }

    @Override
    public <T> @NotNull T getSetting(@NotNull Setting<T> setting) throws ErrorParsingSettingException {
        String value = settingValues.get(setting.label());
        if (value == null) throw new ErrorParsingSettingException();

        return setting.createFromSave(value);
    }

    @Override
    public @NotNull Stream<Setting<?>> listSettings() {
        return settingValues
                .keySet()
                .stream()
                .map(Setting::getSetting);
    }

    protected record MapWrapper(@NotNull HashMap<String, String> settingValues) {

    }
}
