package io.github.toberocat.improvedFactions.event;

import io.github.toberocat.improvedFactions.world.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

public class EventExecutor implements EventListener {

    private static final EventExecutor EVENT_EXECUTOR = new EventExecutor();

    public static EventExecutor getExecutor() {
        return EVENT_EXECUTOR;
    }

    @Override
    public void protectChunk(@NotNull Chunk chunk, @NotNull String registry) {
        new LinkedList<>(EventListener.HANDLER_LIST) // Make save copy
                .forEach(x -> x.protectChunk(chunk, registry));
    }

    @Override
    public void removeProtection(@NotNull Chunk chunk, @Nullable String oldRegistry) {
        new LinkedList<>(EventListener.HANDLER_LIST) // Make save copy
                .forEach(x -> x.removeProtection(chunk, oldRegistry));
    }
}
