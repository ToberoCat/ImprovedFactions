package io.github.toberocat.core.utility.settings.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CallbackSettings extends Setting {
    private static final HashMap<String, ResultCallback<Player>> CALLBACKS = new HashMap<>();

    private String type;

    @Override
    public void fromString(@NotNull String value) {
        // Already implemented it using CALLBACKS Map
    }

    public CallbackSettings(ResultCallback<Player> callback, String settingName, String type, ItemStack display) {
        super(settingName, null, display);
        CALLBACKS.put(settingName, callback);
        this.type = type;
    }

    public void execute(Player player) {
        CALLBACKS.get(settingName).call(player);
    }

    @JsonIgnore
    public String getType() {
        return type;
    }

    @JsonIgnore
    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public ResultCallback<Player> getCallback() {
        return CALLBACKS.get(settingName);
    }

    @JsonIgnore
    public void setCallback(ResultCallback<Player> callback) {
        CALLBACKS.put(settingName, callback);
    }
}
