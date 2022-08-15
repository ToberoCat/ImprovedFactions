package io.github.toberocat.improvedfactions.persistent;

import io.github.toberocat.improvedfactions.MainIF;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpigotPersistentData implements PersistentDataContainer {

    private final MainIF plugin;
    private final org.bukkit.persistence.PersistentDataContainer data;

    public SpigotPersistentData(MainIF plugin, org.bukkit.persistence.PersistentDataContainer data) {
        this.plugin = plugin;
        this.data = data;
    }

    @Override
    public void set(@NotNull String key, @NotNull String value) {
        data.set(plugin.getNamespacedKey(key), PersistentDataType.STRING, value);
    }

    @Override
    public boolean hasString(@NotNull String key) {
        return data.has(plugin.getNamespacedKey(key), PersistentDataType.STRING);
    }

    @Override
    public @Nullable String getString(@NotNull String key) {
        return data.get(plugin.getNamespacedKey(key), PersistentDataType.STRING);
    }

    @Override
    public void remove(@NotNull String key) {
        data.remove(plugin.getNamespacedKey(key));
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

}
