package io.github.toberocat.improvedFactions.handler;

import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ImprovedFactions {

    static @NotNull ImprovedFactions api() {
        ImprovedFactions implementation = ImplementationHolder.improvedFactions;
        if (implementation == null) throw new NoImplementationProvidedException("improved faction");
        return implementation;
    }

    @Nullable FactionPlayer<?> getPlayer(@NotNull UUID uuid);
    @Nullable FactionPlayer<?> getPlayer(@NotNull String name);

}
