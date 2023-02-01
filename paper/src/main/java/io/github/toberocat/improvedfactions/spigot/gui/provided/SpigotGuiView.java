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
import io.github.toberocat.improvedFactions.core.translator.layout.item.XmlItem;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.exception.InvalidJsonGuiRuntimeException;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SpigotGuiView extends AbstractGui { // Todo: Fix gui view
    private final FactionPlayer<?> player;
    private final GuiContent guiContent;
    private final GuiProvider guiProvider;

    public SpigotGuiView(@NotNull Player spigotPlayer, @NotNull GuiContent guiContent) {
        super(spigotPlayer, createInventory(spigotPlayer,
                guiContent.getRows() * 9,
                guiContent.getGuiId()));
        this.player = new SpigotFactionPlayer(spigotPlayer);
        this.guiContent = guiContent;
        this.guiProvider = GuiManager.getGuis().get(guiContent.getGuiId());

        constructGui();
        render();
    }

    private void constructGui() {
        Map<String, ItemState>[][] items = guiContent.getItems();
        for (int i = 0; i < guiContent.getRows(); i++) {
            for (int j = 0; j < items[i].length; j++) {
                Map<String, ItemState> states = items[i][j];
                addSlot(createStack(guiProvider.getState(player, states)), 0, i * 9 + j, u -> {

                });
            }
        }
    }

    private @NotNull ItemStack createStack(@NotNull ItemState state) {
        Material material = getMaterial(state).orElseThrow(() ->
                new InvalidJsonGuiRuntimeException("Material not found for " + state));
        String name = getName(state).orElseThrow(() ->
                new InvalidJsonGuiRuntimeException("Name not found for " + state));
        String[] lore = getLore(state).orElseThrow(() ->
                new InvalidJsonGuiRuntimeException("Lore not found for " + state));
        return material == Material.PLAYER_HEAD
                ? ItemUtils.createHead(state.getCustomData(), 1, name, lore)
                : ItemUtils.createItem(material, 1, name, lore);
    }

    private @NotNull Optional<Material> getMaterial(@NotNull ItemState state) {
        return Optional.ofNullable(Material
                .getMaterial(state
                        .getId()
                        .replaceAll(" ", "_")
                        .toUpperCase()
                ));
    }

    private @NotNull Optional<String> getName(@NotNull ItemState state) {
        return Optional.ofNullable(player
                .getMessage(translatable -> {
                    XmlItem item = translatable
                            .getGuis()
                            .get(guiContent.getGuiId())
                            .get(state.getTranslationId());
                    if (item == null)
                        throw new InvalidJsonGuiRuntimeException("Couldn't find gui item translation in language " +
                                "file for player " + player.getName());
                    return item.title();
                }));
    }

    private @NotNull Optional<String[]> getLore(@NotNull ItemState state) {
        return Optional.ofNullable(player
                .getMessageBatch(translatable -> {
                    XmlItem item = translatable
                            .getGuis()
                            .get(guiContent.getGuiId())
                            .get(state.getTranslationId());
                    if (item == null)
                        throw new InvalidJsonGuiRuntimeException("Couldn't find gui item translation in language " +
                                "file for player " + player.getName());
                    return item.description()
                            .toArray(String[]::new);
                }));
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
