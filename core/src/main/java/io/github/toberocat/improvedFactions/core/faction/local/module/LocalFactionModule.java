package io.github.toberocat.improvedFactions.core.faction.local.module;

import io.github.toberocat.improvedFactions.core.faction.components.FactionModule;
import io.github.toberocat.improvedFactions.core.faction.local.LocalFaction;

public abstract class LocalFactionModule extends FactionModule<LocalFaction> {
    public LocalFactionModule(LocalFaction faction) {
        super(faction);
    }
}
