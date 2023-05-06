package io.github.toberocat.improvedFactions.core.exceptions.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class IllegalFactionNamingException extends FactionException {

    private final @Nullable String name;

    public IllegalFactionNamingException(@NotNull Faction<?> faction, @Nullable String name) {
        super(faction, "exceptions.illegal-faction-name", new PlaceholderBuilder()
                .placeholder("name", Objects.requireNonNullElse(name, "name"))
                .getPlaceholders());
        this.name = name;
    }

    public @Nullable String getName() {
        return name;
    }
}
