package io.github.toberocat.improvedFactions.core.exceptions.faction;

import org.jetbrains.annotations.NotNull;

public class FactionIsFrozenException extends Exception {

    private final String registry;

    public FactionIsFrozenException(@NotNull String registry) {
        super("You can't make any actions on faction " + registry + ", due to it being frozen");
        this.registry = registry;
    }

    public String getRegistry() {
        return registry;
    }
}
