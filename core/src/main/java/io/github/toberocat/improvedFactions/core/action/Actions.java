package io.github.toberocat.improvedFactions.core.action;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actions {

    private final List<String> strings;
    private final Map<String, String> placeholders = new HashMap<>();

    public Actions(@NotNull List<String> strings) {
        this.strings = strings;
    }

    public Actions(@NotNull String string) {
        this(new ArrayList<>(List.of(string)));
    }

    public Actions() {
        this(new ArrayList<>());
    }

    public void add(@NotNull String string) {
        strings.add(string);
    }

    public @NotNull Actions placeholder(@Nullable String what, @Nullable String with) {
        placeholders.put(what, with);
        return this;
    }

    public @NotNull Actions placeholders(@NotNull Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    /**
     * @param commandSender the sender executing
     * @return true if all actions succeeded with their execution
     */
    public boolean run(@NotNull CommandSender commandSender) {
        boolean success = true;

        for (String string : strings) {
            string = StringUtils.replace(string, placeholders);
            success = success && ActionCore.run(string, commandSender);
        }

        return success;
    }
}
