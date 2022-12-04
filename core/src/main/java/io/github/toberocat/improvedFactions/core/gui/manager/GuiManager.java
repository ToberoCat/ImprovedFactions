package io.github.toberocat.improvedFactions.core.gui.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.toberocat.improvedFactions.core.gui.content.GuiContent;
import io.github.toberocat.improvedFactions.core.gui.content.ItemState;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiImplementation;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GuiManager { // ToDo: Get rid of the static method spam and make the code much cleaner

    public static final String SETTINGS_GUI = "manage-faction";
    public static final String PLAYER_ICON_DESIGNER_INV = "sender-inv-banner-designer";

    private static final List<String> guis = new LinkedList<>();

    public static @NotNull List<String> getGuis() {
        return guis;
    }

    public static void registerGui(@NotNull String gui) {
        guis.add(gui);
    }

    public static void openGui(@NotNull String guiId, @NotNull FactionPlayer<?> player) {
        GuiImplementation implementation = ImplementationHolder.guiImplementation;
        if (implementation == null) return;

        implementation.openGui(player, guiId);
    }

    public static void createGuiFromEditor(@NotNull String editorBase64Gui) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(editorBase64Gui);
        GuiContent gui = Json.parse(GuiContent.class, new String(decodedBytes));
        Json.writeToFile(getFile(gui.getGuiId()), gui);
    }

    public static @NotNull GuiContent getGui(@NotNull String guiId) {
        try {
            GuiContent gui = Json.parse(GuiContent.class,
                    getFile(guiId));
            gui.setGuiId(guiId);
            return gui;
        } catch (IOException e) {
            return new GuiContent(guiId, 6, new String[]{"defaultState"}, new HashMap[9][6]);
        }
    }

    private static @NotNull File getFile(@NotNull String guiId) {
        return new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui");
    }

    static {
        registerGui(SETTINGS_GUI);
        registerGui(PLAYER_ICON_DESIGNER_INV);
    }
}
