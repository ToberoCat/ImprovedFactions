package io.github.toberocat.improvedFactions.core.gui.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class GuiContent {
    private String guiId;
    private int rows;
    private String[] states;
    private Map<String, ItemState>[][] items;

    public GuiContent() {
    }

    public GuiContent(String guiId, int rows, String[] states,  Map<String, ItemState>[][] items) {
        this.guiId = guiId;
        this.rows = rows;
        this.states = states;
        this.items = items;
    }

    @JsonIgnore
    public @NotNull String getItemAt(int index) {
        return getItemAt(index, "defaultState");
    }


    @JsonIgnore
    public @NotNull String getItemAt(int index, @NotNull String state) {
        int row = index / 9;
        int column = index % 9;

        return items[row][column].get("defaultState").getId(); // ToDo: Fix it with translation
    }

    public String getGuiId() {
        return guiId;
    }

    public void setGuiId(String guiId) {
        this.guiId = guiId;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public Map<String, ItemState>[][] getItems() {
        return items;
    }

    public void setItems(Map<String, ItemState>[][] items) {
        this.items = items;
    }
}
