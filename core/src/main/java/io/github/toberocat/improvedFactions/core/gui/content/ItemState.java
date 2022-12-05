package io.github.toberocat.improvedFactions.core.gui.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemState {
    private String id;
    private String translationId;

    private String customData;

    public ItemState() {

    }

    public ItemState(@NotNull String id, @NotNull String translationId) {
        this.id = id;
        this.translationId = translationId;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }
}
