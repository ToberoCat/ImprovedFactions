/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.action.provided.faction;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.ActionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class FactionIconAction extends Action {
    /**
     * Gets the text in the brackets at the beginning of the action.
     * Should not have uppercase letters or spaces and excludes the brackets.
     *
     * @return the label for this action.
     */
    @Override
    public @NotNull String label() {
        return "faction-edit-icon";
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player) {
        ActionHandler.api().changeFactionIcon(player);
    }
}
