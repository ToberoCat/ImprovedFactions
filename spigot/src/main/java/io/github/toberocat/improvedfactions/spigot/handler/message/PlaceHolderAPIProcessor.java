/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.handler.message;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

import java.util.function.BiFunction;

public class PlaceHolderAPIProcessor implements BiFunction<OfflinePlayer, String, String> {

    /**
     * Applies this function to the given arguments.
     *
     * @param player the first function argument
     * @param text      the second function argument
     * @return the function result
     */
    @Override
    public String apply(OfflinePlayer player, String text) {
        return PlaceholderAPI.setPlaceholders(player, text);

    }
}
