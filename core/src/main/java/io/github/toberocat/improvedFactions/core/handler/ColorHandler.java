package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface ColorHandler {

    static @NotNull ColorHandler api() {
        ColorHandler implementation = ImplementationHolder.colorHandler;
        if (implementation == null) throw new NoImplementationProvidedException("color handler");
        return implementation;
    }

    @NotNull String stripColor(@NotNull String text);

    @NotNull String format(@NotNull String text);
}
