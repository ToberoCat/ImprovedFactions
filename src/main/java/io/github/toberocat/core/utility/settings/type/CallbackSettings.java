package io.github.toberocat.core.utility.settings.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.utility.callbacks.ResultCallback;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CallbackSettings extends Setting {

    private String type;
    private ResultCallback<Player> callback;

    public CallbackSettings() {
    }

    public CallbackSettings(ResultCallback<Player> callback, String settingName, String type, ItemStack display) {
        super(settingName, null, display);
        this.callback = callback;
        this.type = type;
    }

    public void execute(Player player) {
        callback.call(player);
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
        return callback;
    }

    @JsonIgnore
    public void setCallback(ResultCallback<Player> callback) {
        this.callback = callback;
    }
}
