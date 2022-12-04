package io.github.toberocat.improvedFactions.core.gui.content;

import org.jetbrains.annotations.NotNull;

public class ItemState {
    private String id;
    private String translationId;

    public ItemState() {

    }

    public ItemState(@NotNull String id, @NotNull String translationId) {
        this.id = id;
        this.translationId = translationId;
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
