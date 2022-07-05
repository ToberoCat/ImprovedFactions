package io.github.toberocat.core.utility.dynamic.loaders;

import io.github.toberocat.MainIF;
import org.bukkit.event.Listener;

import java.util.LinkedList;

import static org.bukkit.Bukkit.getPluginManager;

public abstract class DynamicLoader<T, E> implements Listener {
    private static final LinkedList<DynamicLoader> LOADERS = new LinkedList<>();

    protected static void Subscribe(DynamicLoader loader) {
        getPluginManager().registerEvents(loader, MainIF.getIF());
        LOADERS.add(loader);
    }

    public static void disable() {
        for (DynamicLoader loader : LOADERS) {
            loader.Disable();
        }
        LOADERS.clear();
    }

    public static void enable() {
        for (DynamicLoader loader : LOADERS) {
            loader.Enable();
        }
    }

    protected abstract void loadPlayer(final T value);

    protected abstract void unloadPlayer(final E value);

    protected abstract void Disable();

    protected abstract void Enable();

}
