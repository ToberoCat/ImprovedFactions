package io.github.toberocat.improvedFactions.core.gui.manager;

import io.github.toberocat.improvedFactions.core.gui.content.Flag;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class AbstractGuiProvider implements GuiProvider {
    private final Flag[] flags;
    private final String[] states;

    protected String guiId;

    public AbstractGuiProvider(@NotNull String guiId, @NotNull GuiSettings builder) {
        this.guiId = guiId;
        this.flags = builder.flags.toArray(Flag[]::new);
        this.states = builder.states.toArray(String[]::new);
    }

    @Override
    public @NotNull String getGuiId() {
        return guiId;
    }

    @Override
    public @NotNull String[] getStates() {
        return states;
    }

    @Override
    public @NotNull Flag[] getFlags() {
        return flags;
    }

    public static class GuiSettings {
        protected List<Flag> flags;
        protected Set<String> states;

        public GuiSettings() {
            flags = new ArrayList<>();
            states = new HashSet<>(List.of("defaultState"));
        }

        public @NotNull GuiSettings addFlag(@NotNull String name) {
            return addFlag(name, flags.size());
        }

        public @NotNull GuiSettings addFlag(@NotNull String name, int id) {
            flags.add(new Flag(name, id));
            return this;
        }

        public @NotNull GuiSettings addState(@NotNull String... state) {
            states.addAll(List.of(state));
            return this;
        }
    }
}
