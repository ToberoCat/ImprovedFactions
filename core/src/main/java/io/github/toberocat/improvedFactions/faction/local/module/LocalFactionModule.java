package io.github.toberocat.improvedFactions.faction.local.module;

import io.github.toberocat.improvedFactions.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.faction.local.LocalFaction;
import org.jetbrains.annotations.NotNull;

public abstract class LocalFactionModule extends FactionModule<LocalFaction> {
    public LocalFactionModule(LocalFaction faction) {
        super(faction);
    }
}
