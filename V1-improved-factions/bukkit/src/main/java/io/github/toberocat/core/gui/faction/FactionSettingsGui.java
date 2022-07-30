package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

// ToDo: Add new gui for managing relations
public class FactionSettingsGui extends TabbedGui {
    public FactionSettingsGui(Player player) {
        super(player, createInventory(player));

        Faction faction = FactionManager.getPlayerFaction(player);
        assert faction != null;

        renderGui(faction.getFactionPerm().getFactionSettings(), player);
    }

    private static @NotNull Inventory createInventory(Player player) {
        Faction faction = FactionManager.getPlayerFaction(player);
        if (faction == null) return Bukkit.createInventory(player, 54, "§cError - No faction");

        return Bukkit.createInventory(player, 54, "§e§l" + faction.getDisplayName() + "'s §esettings");
    }

    private void renderGui(Map<String, Setting> settings, Player player) {
        for (Setting factionSet : settings.values()) {
            addSlot(Setting.getSlot(factionSet, player, () -> {
                clear();
                renderGui(settings, player);
            }));
        }
        render();
    }
}
