package io.github.toberocat.improvedFactions.core.faction.components;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class FactionModule<F extends Faction<F>> {
    private final F faction;

    public FactionModule(F faction) {
        this.faction = faction;
    }

    public F faction() {
        return faction;
    }

    public abstract @NotNull String registry();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        var that = (FactionModule<?>) obj;
        return Objects.equals(faction, that.faction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(faction);
    }

    @Override
    public String toString() {
        return "FactionModule[" +
                "faction=" + faction + ']';
    }

}
