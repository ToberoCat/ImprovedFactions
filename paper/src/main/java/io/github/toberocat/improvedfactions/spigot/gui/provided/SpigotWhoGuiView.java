package io.github.toberocat.improvedfactions.spigot.gui.provided;

import io.github.toberocat.improvedFactions.core.action.Actions;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.gui.AbstractGui;
import io.github.toberocat.improvedfactions.spigot.gui.page.Page;
import io.github.toberocat.improvedfactions.spigot.gui.settings.GuiSettings;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.utils.YamlLoader;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class SpigotWhoGuiView extends AbstractGui { // ToDo: Make /f who a gui

    private final File files;
    private final FactionPlayer<?> fPlayer;

    public SpigotWhoGuiView(@NotNull Faction<?> faction, @NotNull Player player, GuiContent guiContent) {
        super(player, AbstractGui.createInventory(player, guiContent.getRows() * 9, guiContent.getGuiId()));
        fPlayer = new SpigotFactionPlayer(player);
        files = new File(ImprovedFactions.api().getDataFolder(), "actions/" + guiContent.getGuiId());
        if (!files.exists()) {
            if (!files.mkdir()) Logger.api()
                    .logWarning("Couldn't create gui actions folder for gui " + guiContent.getGuiId());
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
        });*/

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