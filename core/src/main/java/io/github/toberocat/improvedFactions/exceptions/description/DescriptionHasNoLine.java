package io.github.toberocat.improvedFactions.exceptions.description;

import org.jetbrains.annotations.NotNull;

public class DescriptionHasNoLine extends RuntimeException {
    public DescriptionHasNoLine(@NotNull String registry, int line) {
        super(String.format("Description of faction '%s' has no line %d", registry, line));
    }
}
