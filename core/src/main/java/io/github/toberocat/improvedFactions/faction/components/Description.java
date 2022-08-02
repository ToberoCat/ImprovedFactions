package io.github.toberocat.improvedFactions.faction.components;

import io.github.toberocat.improvedFactions.exceptions.description.DescriptionHasNoLine;
import io.github.toberocat.improvedFactions.exceptions.faction.FactionIsFrozenException;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public interface Description {
    /**
     * Returns a stream of lines from the description.
     *
     * @return All lines of the description.
     */
    @NotNull Stream<String> getLines();

    /**
     * Returns the line of the description at the given line number.
     *
     * @param line The line number to get.
     * @return The description content
     */
    @NotNull String getLine(int line) throws DescriptionHasNoLine;

    /**
     * Sets the content of the line at the specified index
     *
     * @param line    The line number to set the content of.
     * @param content The content of the line.
     */
    void setLine(int line, @NotNull String content) throws FactionIsFrozenException;

    /**
     * Returns true if the given line number is in the file.
     *
     * @param line The line number to check.
     * @return If the line exists in this description instance
     */
    boolean hasLine(int line);

    /**
     * Returns the last line of the description.
     *
     * @return The last line of the description.
     */
    int getLastLine();
}
