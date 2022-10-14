package io.github.toberocat.improvedFactions.core.action.provided.faction;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.ActionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class FactionDescriptionAction extends Action {
    /**
     * Gets the text in the brackets at the beginning of the action.
     * Should not have uppercase letters or spaces and excludes the brackets.
     *
     * @return the label for this action.
     */
    @Override
    public @NotNull String label() {
        return "faction-description";
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player) {
        ActionHandler.api().changeDescription(player);
    }
}
