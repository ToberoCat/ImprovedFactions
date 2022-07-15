package io.github.toberocat.core.factions;

import io.github.toberocat.core.utility.exceptions.DescriptionHasNoLine;
import org.jetbrains.annotations.NotNull;

public interface Description {
    @NotNull String getLine(int line) throws DescriptionHasNoLine;
    void setLine(int line, @NotNull String content);
    boolean hasLine(int line);
    int getLastLine();
}
