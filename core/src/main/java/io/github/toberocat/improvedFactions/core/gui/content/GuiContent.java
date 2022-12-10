package io.github.toberocat.improvedFactions.core.gui.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.toberocat.improvedFactions.core.gui.provided.SettingGui;
import io.github.toberocat.improvedFactions.core.json.Json;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Stream;


public class GuiContent {
    private String guiId;
    private int rows;
    private String[] states;
    private Flag[] flags;
    private Map<String, ItemState>[][] items;

    public GuiContent() {
    }

    public GuiContent(@NotNull String guiId, int rows, Flag[] flags, String... states) {
        this.guiId = guiId;
        this.rows = rows;
        this.states = getStates(states);
        this.flags = flags;
        this.items = createItems();
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(Json.parse(new GuiContent("manage-faction", 6, new Flag[]{
                new Flag("Faction Icon", SettingGui.FACTION_ICON_FLAG)
        }, "test")));
    }

    private @NotNull Map<String, ItemState>[][] createItems() {
        Map<String, ItemState>[][] items = new HashMap[rows][9];

        for (int i = 0; i < rows; i++) {
            items[i] = new HashMap[9];
            for (int j = 0; j < 9; j++) {
                items[i][j] = new HashMap<>();
                for (String state : states)
                    items[i][j].put(state, new ItemState("air",
                            "none",
                            Collections.emptyList()
                    ));
            }
        }

        return items;
    }

    private @NotNull String[] getStates(String... states) {
        return Stream.concat(
                        Stream.of("defaultState"),
                        Stream.of(states)
                ).distinct()
                .toArray(String[]::new);
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

    public Flag[] getFlags() {
        return flags;
    }

    public void setFlags(Flag[] flags) {
        this.flags = flags;
    }
}
