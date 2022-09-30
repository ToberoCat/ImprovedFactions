package io.github.toberocat.improvedFactions.core.gui;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.json.Json;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GuiManager {

    private static final List<String> guis = new LinkedList<>();

    public static @NotNull List<String> getGuis() {
        return guis;
    }

    public static void registerGui(@NotNull String gui) {
        guis.add(gui);
    }

    public static @NotNull JsonGui getGui(@NotNull String guiId) {
        try {
            JsonGui gui = Json.parse(JsonGui.class,
                    new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui"));
            gui.setGuiId(guiId);
            return gui;
        } catch (IOException e) {
            e.printStackTrace();
            return new JsonGui(new HashMap<>(), guiId);
        }
    }
}
