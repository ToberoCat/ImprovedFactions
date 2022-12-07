/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.action.Actions;
import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiProvider;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import io.github.toberocat.improvedfactions.spigot.utils.YamlLoader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public class SpigotGuiView extends AbstractGui { // Todo: Fix gui view

    private final File files;
    private final FactionPlayer<?> fPlayer;

    public SpigotGuiView(@NotNull Player player, @NotNull GuiContent guiContent) {
        super(player, AbstractGui.createInventory(player, guiContent.getRows() * 9, guiContent.getGuiId()));
        fPlayer = new SpigotFactionPlayer(player);
        files = new File(ImprovedFactions.api().getDataFolder(), "actions/" + guiContent.getGuiId());
        if (!files.exists()) {
            if (!files.mkdir()) Logger.api()
                    .logWarning("Couldn't create gui actions folder for gui " + guiContent.getGuiId());
        }

        Map<String, ItemState>[][] items = guiContent.getItems();
        GuiProvider provider = GuiManager.getGuis().get(guiContent.getGuiId());
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                Map<String, ItemState> states = items[i][j];
                ItemState state = provider.getState(states);
                Material material = Material.getMaterial(state
                        .getId()
                        .replaceAll(" ", "_")
                        .toUpperCase()
                );
                ItemStack stack = ItemUtils.createItem(material, 1, fPlayer
                                .getMessage(translatable -> translatable
                                        .getGuis()
                                        .get(guiContent.getGuiId())
                                        .get(state.getTranslationId())
                                        .title()),
                        fPlayer.getMessageBatch(translatable -> translatable
                                .getGuis()
                                .get(guiContent.getGuiId())
                                .get(state.getTranslationId())
                                .description()
                                .toArray(String[]::new)));
                addSlot(stack, 0, i * 9 + j, u -> {

                });
            }
        }

/*

        guiContent.getContent().forEach((item, action) -> {
            if (item.slot() >= inventory.getSize()) return;

            ItemStack stack = (ItemStack) item.stack().getRaw();
            ItemMeta meta = stack.getItemMeta();
            if (meta == null) return;

            String id = meta.getDisplayName();
            MessageHandler api = MessageHandler.api();
            meta.setDisplayName(api.format(fPlayer, Objects.requireNonNullElse(fPlayer.getMessage(translatable -> translatable.getItems()
                    .get(guiContent.getGuiId())
                    .get(id)
                    .title()), "")));

            meta.setLore(Arrays.stream(fPlayer.getMessageBatch(translatable -> translatable.getItems()
                            .get(guiContent.getGuiId())
                            .get(id).description()
                            .stream()
                            .map(x -> api.format(fPlayer, x))
                            .toArray(String[]::new)))
                    .toList());

            stack.setItemMeta(meta);

            addSlot(new Slot(stack) {
                @Override
                public void leftClick(@NotNull Player sender, @Nullable ItemStack cursor) {
                    callAction(id, "left-click");
                }

                @Override
                public void shiftLeftClick(@NotNull Player sender, @Nullable ItemStack cursor) {
                    callAction(id, "shift-left-click");
                }

                @Override
                public void rightClick(@NotNull Player sender, @Nullable ItemStack cursor) {
                    callAction(id, "right-click");
                }

                @Override
                public void shiftRightClick(@NotNull Player sender, @Nullable ItemStack cursor) {
                    callAction(id, "shift-right-click");
                }
            }, 0, item.slot());
        });
*/

        render();
    }

    private void callAction(@NotNull String id, @NotNull String actionPath) {
        File file = new File(files, id + ".yml");
        if (!file.exists()) return;
        List<String> actions = new YamlLoader(file, JavaPlugin.getPlugin(MainIF.class))
                .load()
                .fileConfiguration()
                .getStringList(actionPath);
        System.out.println(actions.size());
        new Actions(actions)
                .run(fPlayer);
    }

    @Override
    protected void addPage() {
        pages.add(new Page(inventory.getSize()));
    }

    @Override
    protected GuiSettings readSettings() {
        return new GuiSettings();
    }
}
