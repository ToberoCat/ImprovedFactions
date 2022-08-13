package io.github.toberocat.improvedFactions.event;

import io.github.toberocat.improvedFactions.world.Chunk;
import org.jetbrains.annotations.NotNull;

public class EventAdapter implements EventListener {

    public EventAdapter() {
        EventListener.listen(this);
    }
    @Override
    public void protectChunk(@NotNull Chunk chunk, @NotNull String registry) {

    }
}
