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
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
            if (item.slot() >= slotActions.length) return;

            slotActions[item.slot()] = action;
            ItemStack stack = (ItemStack) item.stack().getRaw();
            ItemMeta meta = stack.getItemMeta();
            if (meta != null) {
                FactionPlayer<Player> fPlayer = new SpigotFactionPlayer(player);
                meta.getPersistentDataContainer()
                        .set(ORIGINAL_NAME_KEY, PersistentDataType.STRING,
                                meta.getDisplayName());

                String id = meta.getDisplayName();
                meta.setDisplayName(fPlayer.getMessage(translatable -> translatable.getItems()
                        .get(jsonGui.getGuiId())
                        .get(id)
                        .title()));
                meta.setLore(Arrays.stream(fPlayer.getMessageBatch(translatable -> translatable.getItems()
                        .get(jsonGui.getGuiId())
                        .get(id).description().toArray(String[]::new))).toList());
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
        return createInventory(player, clamp(jsonGui.getRows() * 9, 9, 54), jsonGui.getTitle());
    }

    private void updateContent() {
        io.github.toberocat.improvedFactions.core.item.ItemStack[] stacks = Arrays.stream(inventory.getContents())
                .peek(x -> {
                    if (x == null) return;

                    ItemMeta meta = x.getItemMeta();
                    if (meta != null) {
                        String old = meta.getPersistentDataContainer()
                                .get(ORIGINAL_NAME_KEY, PersistentDataType.STRING);
                        if (old == null) return;

                        meta.getPersistentDataContainer()
                                .remove(ORIGINAL_NAME_KEY);

                        meta.setDisplayName(old);
                        meta.setLore(List.of());
                    }
                    x.setItemMeta(meta);
                })
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
        pages.add(new Page(inventory.getSize()));
    }

    @Override
    public void click(InventoryClickEvent event) {
        super.click(event);
        if (event.getCursor() != null) event.setCancelled(false);
    }

    @Override
    public void drag(InventoryDragEvent event) {
        super.drag(event);
        if (event.getCursor() != null) event.setCancelled(false);
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings();
    }
}
