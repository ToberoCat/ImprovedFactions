package io.github.toberocat.improvedFactions.core.gui.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemState {
    private String id;
    private String translationId;
    private String customData;

    private List<Integer> flags;

    public ItemState() {

    }

    public ItemState(@NotNull String id,
                     @NotNull String translationId,
                     @NotNull List<Integer> flags) {
        this.id = id;
        this.translationId = translationId;
        this.flags = flags;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public boolean isFlagSet(@NotNull String flagId) {
        return flags.contains(flagId);
    }

    public List<Integer> getFlags() {
        return flags;
    }

    public void setFlags(List<Integer> flags) {
        this.flags = flags;
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
