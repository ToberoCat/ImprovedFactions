package io.github.toberocat.improvedFactions.core.gui.manager;

import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.gui.provided.SettingGui;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GuiManager { // ToDo: Get rid of the static method spam and make the code much cleaner
    private static final Map<String, GuiProvider> guis = new HashMap<>();

    static {
        registerGui(new SettingGui());
    }

    public static @NotNull Map<String, GuiProvider> getGuis() {
        return guis;
    }

    public static void registerGui(@NotNull GuiProvider guiProvider) {
        guis.put(guiProvider.getGuiId(), guiProvider);
    }

    public static void openGui(@NotNull String guiId, @NotNull FactionPlayer<?> player) {
        GuiImplementation implementation = ImplementationHolder.guiImplementation;
        if (implementation == null) return;

        implementation.openGui(player, guiId);
    }

    public static @NotNull GuiContent getGui(@NotNull String guiId) {
        try {
            GuiContent gui = Json.parse(GuiContent.class,
                    getFile(guiId));
            gui.setGuiId(guiId);
            return gui;
        } catch (IOException e) {
            throw new RuntimeException(e);
            //  return new GuiContent(guiId, 6);
        }
    }

    private static @NotNull File getFile(@NotNull String guiId) {
        return new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui");
    }
}
