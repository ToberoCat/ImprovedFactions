package io.github.toberocat.improvedFactions.core.gui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonGui {
    private Map<ItemStack, String> content;

    private String guiId;

    protected JsonGui() {

    }

    protected JsonGui(@NotNull Map<ItemStack, String> content, @NotNull String guiId) {
        this.content = content;
        this.guiId = guiId;
    }

    public static @Nullable JsonGui loadGui(@NotNull String guiId) {
        try {
            JsonGui gui = Json.parse(JsonGui.class,
                    new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui"));
            gui.setGuiId(guiId);

            return gui;
        } catch (IOException e) {
            Logger.api().logException(e);
            return null;
        }
    }

    public void writeToFile() {

    }

    public Map<ItemStack, String> getContent() {
        return content;
    }

    public void setContent(Map<ItemStack, String> content) {
        this.content = content;
    }

    @JsonIgnore
    public String getGuiId() {
        return guiId;
    }

    @JsonIgnore
    public void setGuiId(String guiId) {
        this.guiId = guiId;
    }
}
