package io.github.toberocat.improvedfactions.spigot.gui;

import io.github.toberocat.guiengine.api.GuiEngineApi;
import io.github.toberocat.guiengine.api.components.GuiComponent;
import io.github.toberocat.guiengine.api.components.Selectable;
import io.github.toberocat.guiengine.api.context.GuiContext;
import io.github.toberocat.guiengine.api.event.spigot.GuiCloseEvent;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.OpenType;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

public class GuiApi implements io.github.toberocat.improvedFactions.core.gui.GuiApi {
    private final GuiEngineApi api;

    public GuiApi(@NotNull JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "guis");
        this.api = new GuiEngineApi("factions", file);
        this.api.reload();
    }

    @Override
    public void openGui(@NotNull FactionPlayer player, @NotNull String guiId) {
        if (!(player.getRaw() instanceof Player bukkitPlayer))
            return;
        api.openGui(bukkitPlayer, guiId);
    }

    @Override
    public void openGui(@NotNull FactionPlayer player,
                        @NotNull String guiId,
                        @NotNull Map<String, String> placeholders) {
        if (!(player.getRaw() instanceof Player bukkitPlayer))
            return;
        api.openGui(bukkitPlayer, guiId, placeholders);
    }

    @Override
    public void openSettingsGui(@NotNull FactionPlayer player) {
        if (!(player.getRaw() instanceof Player bukkitPlayer))
            return;
        GuiContext context;
        try {
            context = api.openGui(bukkitPlayer, "settings", PlaceholderBuilder
                    .extractMethods("faction", player.getFaction()));
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            throw new RuntimeException(e);
        }

        GuiComponent component = context.findComponentById("faction-open-type");
        if (!(component instanceof Selectable selectable)) {
            bukkitPlayer.sendMessage("§cThe gui won't function properly. A component with id " +
                    "§6faction-open-type§c is missing or isn't selectable");
            return;
        }
        context.listen(GuiCloseEvent.class, (event) -> {
            String[] values = selectable.getSelectionModel();
            OpenType openType = OpenType.valueOf(values[selectable.getSelected()]);
            System.out.println("Closed gui: " + openType);
        });
    }
}
