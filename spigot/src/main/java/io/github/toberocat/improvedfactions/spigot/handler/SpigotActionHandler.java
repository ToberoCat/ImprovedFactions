/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.handler.ActionHandler;
import io.github.toberocat.improvedFactions.core.handler.MessageHandler;
import io.github.toberocat.improvedFactions.core.permission.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record SpigotActionHandler(@NotNull MainIF plugin) implements ActionHandler {
    @Override
    public void renameFaction(@NotNull FactionPlayer<?> player) {
        Faction<?> faction;
        try {
            faction = player.getFaction();
            if (!faction.hasPermission(FactionPermission.RENAME_FACTION, player))
                return;
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return;
        }

        new AnvilGUI.Builder()
                .onComplete((u, text) -> {
                    try {
                        faction.setDisplay(MessageHandler.api().format(text));
                    } catch (FactionIsFrozenException e) {
                        e.printStackTrace();
                    }
                    return AnvilGUI.Response.close();
                })
                .text(faction.getDisplay().replaceAll("§", "&"))
                .itemLeft(ItemUtils.createItem(Material.WHITE_BANNER, faction.getDisplay()))
                .plugin(plugin)
                .open((Player) player.getRaw());
    }
}
