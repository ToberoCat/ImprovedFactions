package io.github.toberocat.improvedFactions.core.persistent.local;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.persistent.PersistentData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LocalData implements PersistentData {

    private final Map<UUID, Values> entries;
    private final FileAccess access;

    public LocalData() {
        this.entries = new HashMap<>();
        this.access = new FileAccess(ImprovedFactions.api().getLocalDataFolder());
    }

    @Override
    public @Nullable Object set(@NotNull UUID id, @NotNull String key, @NotNull Object value) {
        return getValues(id).getRawMap().put(key, value);
    }

    @Override
    public @Nullable Object remove(@NotNull UUID id, @NotNull String key) {
        return getValues(id).getRawMap().remove(key);
    }

    @Override
    public @Nullable Object get(@NotNull UUID id, @NotNull String key) {
        return getValues(id).getRawMap().get(key);
    }

    @Override
    public boolean has(@NotNull UUID id, @NotNull String key) {
        return getValues(id).getRawMap().containsKey(key);
    }

    @Override
    public void dispose() {
        entries.forEach(this::save);
        entries.clear();
    }

    @Override
    public void quit(@NotNull FactionPlayer<?> player) {
        Values values = entries.get(player.getUniqueId());
        if (values == null) return;

        save(player.getUniqueId(), values);
    }

    private @NotNull Values getValues(@NotNull UUID id) {
        Values values = entries.get(id);
        if (values == null) {
            values = load(id);
            if (values == null) values = new Values();

            entries.put(id, values);
        }

        return values;
    }

    private @Nullable Values load(@NotNull UUID id) {
        try {
            return access.read(Values.class, FileAccess.PERSISTENT_FOLDER, id + ".json");
        } catch (IOException e) {
            return null;
        }
    }

    private void save(@NotNull UUID id, @NotNull Values values) {
        try {
            access.write(values, FileAccess.PERSISTENT_FOLDER, id + ".json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Values {
        private Map<String, Object> rawMap = new HashMap<>();

        public Map<String, Object> getRawMap() {
            return rawMap;
        }

        public void setRawMap(Map<String, Object> rawMap) {
            this.rawMap = rawMap;
        }
    }
}
