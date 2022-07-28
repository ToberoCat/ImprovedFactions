package io.github.toberocat.core.player;

import io.github.toberocat.core.utility.settings.type.Setting;
import io.github.toberocat.core.utility.settings.type.SettingEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSettings {

    private UUID playerUUID;
    private Map<String, Setting> playerSettings;

    public PlayerSettings() {
    }

    protected PlayerSettings(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.playerSettings = new HashMap<>(PlayerSettingHandler.DEFAULT_SETTINGS);
    }

    public <T> Setting<T> get(String setting) {
        return playerSettings.get(setting);
    }


    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public Map<String, Setting> getPlayerSettings() {
        return playerSettings;
    }

    public void setPlayerSettings(Map<String, Setting> playerSettings) {
        this.playerSettings = playerSettings;
    }
}