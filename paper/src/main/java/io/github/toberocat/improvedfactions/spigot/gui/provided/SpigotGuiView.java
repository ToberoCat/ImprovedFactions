/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiProvider;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SpigotGuiView extends AbstractGui { // Todo: Fix gui view
    private final FactionPlayer<?> player;

    public SpigotGuiView(@NotNull Player spigotPlayer, @NotNull GuiContent guiContent) {
        super(spigotPlayer, createInventory(spigotPlayer,
                guiContent.getRows() * 9,
                guiContent.getGuiId()));
        player = new SpigotFactionPlayer(spigotPlayer);

        Map<String, ItemState>[][] items = guiContent.getItems();
        GuiProvider provider = GuiManager.getGuis().get(guiContent.getGuiId());
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[i].length; j++) {
                Map<String, ItemState> states = items[i][j];
                ItemState state = provider.getState(player, states);
                Material material = Material.getMaterial(state
                        .getId()
                        .replaceAll(" ", "_")
                        .toUpperCase()
                );
                String title = player.getMessage(translatable -> translatable
                        .getGuis()
                        .get(guiContent.getGuiId())
                        .get(state.getTranslationId())
                        .title());

                String[] lore = player.getMessageBatch(translatable -> translatable
                        .getGuis()
                        .get(guiContent.getGuiId())
                        .get(state.getTranslationId())
                        .description()
                        .toArray(String[]::new));

                ItemStack stack;
                if (material != Material.PLAYER_HEAD)
                    stack = ItemUtils.createItem(material, 1, title, lore);
                else {
                    stack = ItemUtils.createHead(state.getCustomData(), 1, title, lore);
                }
                addSlot(stack, 0, i * 9 + j, u -> {

                });
            }
        }

        render();
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
