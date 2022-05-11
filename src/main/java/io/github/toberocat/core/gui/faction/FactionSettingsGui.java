package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class FactionSettingsGui extends Gui {
    public FactionSettingsGui(Player player) {
        super(player, createInventory(player), createSettings());

        Faction faction = FactionUtility.getPlayerFaction(player);
        assert faction != null;

        renderGui(faction.getFactionPerm().getFactionSettings(), player);
    }

    private static GUISettings createSettings() {
        return new GUISettings();
    }

    private static Inventory createInventory(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return null;

        return Bukkit.createInventory(player, 54, "§e§l" + faction.getDisplayName() + "'s §esettings");
    }

    private void renderGui(Map<String, Setting> settings, Player player) {
        for (Setting factionSet : settings.values()) {
            addSlot(Setting.getSlot(factionSet, player, () -> {
                slots.remove(currentPage);
                slots.add(currentPage, new Page());
                renderGui(settings, player);
            }));
        }
    }
}
