package io.github.toberocat.improvedFactions.core.hints;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Hints {
    
    public static void playHint(@NotNull Runnable playHint) {
        playHint.run(); // ToDo: This will be extended in future with options to disable / daily hints
    }

    public static void playHint(@NotNull FactionPlayer player,
                                @NotNull String hintId,
                                @NotNull Map<String, Function<Translatable, String>> placeholders) {
        playHint(() -> player.sendMessage("hints." + hintId, placeholders));
    }

    public static void playHint(@NotNull FactionPlayer player,
                                @NotNull String hintId) {
        playHint(() -> player.sendMessage("hints." + hintId, new HashMap<>()));
    }
}
