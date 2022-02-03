package io.github.toberocat.core.utility.data;

import io.github.toberocat.MainIF;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PersistentDataUtility {
    public static final NamespacedKey FACTION_CLAIMED_KEY = new NamespacedKey(MainIF.getIF(), "faction-claimed");
    public static final NamespacedKey PLAYER_FACTION_REGISTRY = new NamespacedKey(MainIF.getIF(), "if-joined-faction");

    public static <T extends PersistentDataContainer, E> void write(NamespacedKey key, PersistentDataType type, E value, T object) {
        object.set(key, type, value);
    }

    public static <T extends PersistentDataContainer, E> E read(NamespacedKey key, PersistentDataType type, T object) {
        return (E) object.get(key, type);
    }

    public static <T extends PersistentDataContainer, E> boolean has(NamespacedKey key, PersistentDataType type, T object) {
        return object.has(key, type);
    }

    public static <T extends PersistentDataContainer, E> void remove(NamespacedKey key, T object) {
        object.remove(key);
    }
}
