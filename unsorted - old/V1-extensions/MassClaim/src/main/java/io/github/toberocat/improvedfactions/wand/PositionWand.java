package io.github.toberocat.improvedfactions.wand;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.FactionSettings;
import io.github.toberocat.core.utility.settings.type.CallbackSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class PositionWand {

    public static final ArrayList<UUID> USER_ITEMS = new ArrayList<>();
    public static final ItemStack wand = Utility.createItem(Material.STICK, "&eClaim tool");

    public static void dispose() {
        USER_ITEMS.forEach((uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            removePlayer(player);
        }));
        USER_ITEMS.clear();
    }

    public static void removePlayer(Player player) {
        player.getInventory().remove(wand);
    }

    public void register() {
        FactionSettings.add(new CallbackSettings(this::giveUserItem, "claim?chunks", "Item",
                Utility.createItem(Material.STICK, "&eClaim chunks")));
    }

    public void giveUserItem(Player player) {
        player.closeInventory();

        if (PositionWand.USER_ITEMS.contains(player.getUniqueId())) return;

        int empty = getEmptySlots(player.getInventory());
        if (empty == 0) {
            Language.sendMessage("command.claim-mass.hotbar-full", player);
            return;
        }
        USER_ITEMS.add(player.getUniqueId());

        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) != null) continue;
            player.getInventory().setItem(i, wand);
            break;
        }

        Language.sendMessage("command.claim-mass.item-added", player);
    }


    private int getEmptySlots(Inventory inventory) {
        int empties = 0;
        for (int i = 0; i < 9; i++) {
            if (inventory.getItem(i) == null)
                empties++;
        }

        return empties;
    }
}
