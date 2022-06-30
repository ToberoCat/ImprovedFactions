package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.TabbedGui;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageRankSettingGui extends TabbedGui {

    private final Faction faction;
    private final GuiSettings rankSettings;

    public ManageRankSettingGui(Player player, Faction faction, String permission, RankSetting setting, GuiSettings rankGuiSettings) {
        super(player, createInventory(player, permission));
        this.faction = faction;
        this.rankSettings = rankGuiSettings;

        render(player, faction, permission, setting);
    }

    private static Inventory createInventory(Player player, String permission) {
        return Bukkit.createInventory(player, 54, "§e§l" + permission);
    }

    @Override
    protected GuiSettings readSettings() {
        return super.readSettings()
                .setQuitGui(() -> new RankGui(player, faction, rankSettings::getQuitGui));
    }

    private void render(Player player, Faction faction, String permission, RankSetting setting) {
        clear();

        for (Rank rank : Rank.ranks) {
            ItemStack stack = rank.getItem(player);
            List<String> lore = Utility.getLore(stack);
            lore.add("§7Type: §eSelector");
            lore.add(selected(rank, setting) ? "§a§nenabled" : "§aenabled");
            lore.add(selected(rank, setting) ? "§cdisable" : "§c§ndisable");

            addSlot(Utility.setLore(stack, lore.toArray(String[]::new)), (user) -> {
                List<String> selected = new ArrayList<>(List.of(setting.getSelected()));
                String registry = rank.getRegistryName();
                if (selected.contains(registry))
                    selected.remove(registry);
                else selected.add(registry);

                setting.setSelected(selected.toArray(String[]::new));

                render(player, faction, permission, setting);
            });
        }
    }

    private boolean selected(Rank rank, RankSetting setting) {
        return Arrays.stream(setting.getSelected()).filter(x -> x.equals(rank.getRegistryName()))
                .toArray(String[]::new).length > 0;
    }
}
