package io.github.toberocat.improvedFactions.event;

import io.github.toberocat.improvedFactions.world.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * This class should emit events to make
 */
public interface EventListener {

    List<EventListener> HANDLER_LIST = new LinkedList<>();

    static void listen(@NotNull EventListener handler) {
        HANDLER_LIST.add(handler);
    }

    void protectChunk(@NotNull Chunk chunk, @NotNull String registry);
}
