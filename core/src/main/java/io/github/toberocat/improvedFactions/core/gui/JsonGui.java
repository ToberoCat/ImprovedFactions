package io.github.toberocat.improvedFactions.core.gui;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.json.Json;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonGui {
    private Map<String, ItemContainer> content;
    private int rows;
    private String title;

    private String guiId;

    protected JsonGui() {

    }

    protected JsonGui(@NotNull Map<String, ItemContainer> content, @NotNull String guiId) {
        this.content = content;
        this.guiId = guiId;
        rows = 6;
        title = guiId;
    }

    public void write() {
        try {
            Json.writeToFile(new File(ImprovedFactions.api().getGuiFolder(), guiId + ".gui"), this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, ItemContainer> getContent() {
        return content;
    }

    public void setContent(Map<String, ItemContainer> content) {
        this.content = content;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
