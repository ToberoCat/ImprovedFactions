package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.nio.Buffer;
import java.util.UUID;

public class MemberGui extends Gui {
    public MemberGui(Player player, Faction faction, GUISettings settings) {
        super(player, createInventory(player, faction), settings);

        for (UUID uuid : faction.getFactionMemberManager().getMembers()) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
            String lastTimeSeen = off.isOnline() ? "§aOnline" : "§e"+Utility.getTime(off.getLastPlayed());

            AddSlot(Utility.getSkull(off, 1, "§e"+off.getName(), new String[]{
                    "§8Last time seen: " + lastTimeSeen
            }), () -> {});
        }
    }

    private static Inventory createInventory(Player player, Faction faction) {
        return Bukkit.createInventory(player, 54, "§e" + faction.getDisplayName() + "'s members");
    }
}
