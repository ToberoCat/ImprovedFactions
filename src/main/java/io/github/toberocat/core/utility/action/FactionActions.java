package io.github.toberocat.core.utility.action;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactionActions {

    private final List<String> strings;
    private final Map<String, String> placeholders = new HashMap<>();

    public FactionActions(@NotNull List<String> strings) {
        this.strings = strings;
    }

    public FactionActions(@NotNull String string) {
        this(new ArrayList<>(List.of(string)));
    }

    public FactionActions() {
        this(new ArrayList<>());
    }

    public void add(@NotNull String string) {
        strings.add(string);
    }

    public @NotNull FactionActions placeholder(@Nullable String what, @Nullable String with) {
        placeholders.put(what, with);
        return this;
    }

    public @NotNull FactionActions placeholders(@NotNull Map<String, String> placeholders) {
        this.placeholders.putAll(placeholders);
        return this;
    }

    public void run(@NotNull Faction defaultFaction) {
        strings.forEach(x -> runAs(x, defaultFaction));
    }

    private void runAs(@NotNull String action,  @NotNull Faction def) {
        Faction faction = def;
        if (action.startsWith("{")) {
            int end = action.indexOf("}");
            String asRegistry = action.substring(1, end);
            faction = FactionUtility.getFactionByRegistry(asRegistry);
            action = action.substring(end);
        }

        ActionCore.runAsFaction(action, faction);
    }
}
