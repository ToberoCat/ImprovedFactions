package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.handler.ActionHandler;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public record SpigotActionHandler(@NotNull MainIF plugin) implements ActionHandler {
    @Override
    public void renameFaction(@NotNull FactionPlayer player) {
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
                        faction.renameFaction(MessageHandler.api().format(text));
                    } catch (TranslatableException e) {
                        player.sendException(e);
                    }
                    return AnvilGUI.Response.close();
                })
                .text(faction.getDisplay().replaceAll("§", "&"))
                .itemLeft((ItemStack) faction.getIcon().getRaw())
                .plugin(plugin)
                .open((Player) player.getRaw());
    }

    @Override
    public void changeFactionIcon(@NotNull FactionPlayer player) {
        // ToDo: Open banner editor
    }

    @Override
    public void changeMotd(FactionPlayer player) {
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
                        faction.setMotd(MessageHandler.api().format(text));
                    } catch (FactionIsFrozenException e) {
                        player.sendException(e);
                    }
                    return AnvilGUI.Response.close();
                })
                .text(faction.getDisplay().replaceAll("§", "&"))
                .itemLeft((ItemStack) faction.getIcon().getRaw())
                .plugin(plugin)
                .open((Player) player.getRaw());
    }

    @Override
    public void changeDescription(FactionPlayer player) { // ToDo: Open a description gui
        player.sendMessage("Please use a command. This gui is still under construction");
    }
}
