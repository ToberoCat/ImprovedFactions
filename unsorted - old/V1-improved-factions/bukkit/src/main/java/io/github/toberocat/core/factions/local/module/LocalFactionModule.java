package io.github.toberocat.core.factions.local.module;

import io.github.toberocat.core.factions.components.FactionModule;
import io.github.toberocat.core.factions.local.LocalFaction;
import org.jetbrains.annotations.NotNull;

public class LocalFactionModule extends FactionModule<LocalFaction> {
    public LocalFactionModule(LocalFaction faction) {
        super(faction);
    }

    @Override
    public @NotNull String registry() {
        return null;
    }
}
