package io.github.toberocat.improvedFactions.handler;

import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface ColorHandler {

    static @NotNull ColorHandler api() {
        ColorHandler implementation = ImplementationHolder.colorHandler;
        if (implementation == null) throw new NoImplementationProvidedException("color handler");
        return implementation;
    }

    @NotNull String stripColor(@NotNull String text);
}
