package io.github.toberocat.improvedFactions.utils;

import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.world.World;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public final class CUtils {
    public static URL createUrl(@NotNull String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isWorldAllowed(@NotNull World world) {
        if (ConfigHandler.api().getList("world.enabled-worlds").contains(world.getWorldName())) return true;
        return !ConfigHandler.api().getList("world.disabled-worlds").contains(world.getWorldName());
    }
}
