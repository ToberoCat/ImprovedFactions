package io.github.toberocat.factions.core.wrapper.player;

import java.util.UUID;

public abstract class PlayerWrapper {
    private final UUID uuid;

    public PlayerWrapper(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
