package io.github.toberocat.improvedFactions.core.gui;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GuiManager {

    public static final String SETTINGS_GUI = "manage-faction";

    static {
        registerGui(SETTINGS_GUI);
    }

    private static final List<String> guis = new LinkedList<>();

    public static @NotNull List<String> getGuis() {
        return guis;
    }

    public static void registerGui(@NotNull String gui) {
        guis.add(gui);
    }

    public static void openGui(@NotNull String guiId, @NotNull FactionPlayer<?> player) {

    }

    public static @NotNull JsonGui getGui(@NotNull String guiId) {
        try {
            JsonGui gui = Json.parse(JsonGui.class,
                    new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui"));
            gui.setGuiId(guiId);
            return gui;
        } catch (IOException e) {
            return new JsonGui(new HashMap<>(), guiId);
        }
    }
}
