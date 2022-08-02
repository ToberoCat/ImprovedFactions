package io.github.toberocat.core.factions.components;

import io.github.toberocat.core.factions.Faction;
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
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FactionModule<?>) obj;
        return Objects.equals(this.faction, that.faction);
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
