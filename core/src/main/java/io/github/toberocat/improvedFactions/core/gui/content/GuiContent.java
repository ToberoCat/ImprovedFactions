package io.github.toberocat.improvedFactions.core.gui.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.gui.manager.BrowserGuiImplementation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class GuiContent {
    private String guiId;
    private int rows;
    private String[] states;
    private Map<String, ItemState>[][] items;

    public GuiContent() {
    }

    public GuiContent(@NotNull String guiId, int rows, String... states) {
        this.guiId = guiId;
        this.rows = rows;
        this.states = getStates(states);
        this.items = createItems();
    }

    private @NotNull Map<String, ItemState>[][] createItems() {
        Map<String, ItemState>[][] items = new HashMap[rows][9];

        for (int i = 0; i < rows; i++) {
            items[i] = new HashMap[9];
            for (int j = 0; j < 9; j++) {
                items[i][j] = new HashMap<>();
                for (String state : states)
                    items[i][j].put(state, new ItemState("air", "none"));
            }
        }

        return items;
    }

    private @NotNull String[] getStates(String... states) {
        return Stream.concat(
                        Stream.of("defaultState"),
                        Stream.of(states)
                ).toArray(String[]::new);
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
