package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.ItemContainer;
import io.github.toberocat.improvedFactions.core.gui.JsonGui;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.item.SpigotItemStack;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SpigotEditJsonGui extends AbstractGui {

    private static final NamespacedKey ORIGINAL_NAME_KEY = new NamespacedKey(MainIF.getPlugin(MainIF.class),
            "original_naming");

    private final String[] slotActions;
    private final JsonGui jsonGui;

    public SpigotEditJsonGui(@NotNull Player player, @NotNull JsonGui jsonGui) {
        super(player, createInv(player, jsonGui));
        this.slotActions = new String[inventory.getSize()];
        this.jsonGui = jsonGui;

        ItemStack[] content = new org.bukkit.inventory.ItemStack[inventory.getSize()];
        jsonGui.getContent().forEach((item, action) -> {
            slotActions[item.slot()] = action;
            ItemStack stack = (ItemStack) item.stack().getRaw();
            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                FactionPlayer<Player> fPlayer = new SpigotFactionPlayer(player);
                meta.getPersistentDataContainer()
                        .set(ORIGINAL_NAME_KEY, PersistentDataType.STRING,
                                meta.getDisplayName());

                meta.setDisplayName(fPlayer.getMessage(translatable -> translatable.getItems()
                        .get(jsonGui.getGuiId())
                        .get(meta.getDisplayName())
                        .title()));
                meta.setLore(Arrays.stream(fPlayer.getMessageBatch(translatable -> translatable.getItems()
                        .get(jsonGui.getGuiId())
                        .get(meta.getDisplayName())
                        .description().toArray(String[]::new))).toList());
            }
            stack.setItemMeta(meta);

            content[item.slot()] = stack;
        });
        inventory.setContents(content);

        close = () -> {
            updateContent();
            jsonGui.write();
        };
    }

    private static Inventory createInv(@NotNull Player player, @NotNull JsonGui jsonGui) {
        return createInventory(player, 54, jsonGui.getGuiId());
    }

    private void updateContent() {
        io.github.toberocat.improvedFactions.core.item.ItemStack[] stacks = Arrays.stream(inventory.getContents())
                .map(SpigotItemStack::new)
                .toArray(SpigotItemStack[]::new);
        Map<ItemContainer, String> actionMap = new HashMap<>();
        for (int i = 0; i < stacks.length; i++) {
            io.github.toberocat.improvedFactions.core.item.ItemStack stack = stacks[i];
            if (stack == null) continue;

            String actions = slotActions[i];
            actionMap.put(new ItemContainer(i, stack), actions);
        }

        jsonGui.setContent(actionMap);
    }

    @Override
    protected void addPage() {
        MainIF main = MainIF.getPlugin(MainIF.class);
        pages.add(new Page(inventory.getSize(), slot -> {
            ItemStack stack = inventory.getContents()[slot];
            if (stack == null) return;

            String action = slotActions[slot];
            System.out.println(slotActions[slot]);
            new AnvilGUI.Builder()
                    .onComplete((user, text) -> {
                        slotActions[slot] = text;

                        updateContent();
                        Bukkit.getScheduler().runTaskLater(main,
                                () -> new SpigotEditJsonGui(player, jsonGui), 1);
                        return AnvilGUI.Response.close();
                    })
                    .text(action == null ? "no/action" : action)
                    .itemLeft(ItemUtils.createItem(Material.PAPER, "§6§lEnter action path"))
                    .title("§6§lEnter action path")
                    .plugin(main)
                    .open(player);
        }));
    }

    @Override
    public void click(InventoryClickEvent event) {
        super.click(event);
        if (event.getCursor() != null) event.setCancelled(false);
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings();
    }
}
