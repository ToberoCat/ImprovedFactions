package io.github.toberocat.improvedFactions.core.translator;

import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;

public record TranslationFixer(@NotNull Translatable internal,
                               @NotNull Translatable external) {
    public void fix() {
        // Fix rank values
        internal.getRanks()
                .keySet()
                .stream()
                .filter(x -> !external.getRanks().containsKey(x))
                .forEach(x -> external.getRanks().put(x, internal.getRanks().get(x)));

        // Broadcast
        internal.getMessages()
                .getFaction()
                .getBroadcast()
                .keySet()
                .stream()
                .filter(x -> !external
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .containsKey(x))
                .forEach(x -> external
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .put(x, internal
                                .getMessages()
                                .getFaction()
                                .getBroadcast()
                                .get(x)));

        // Players
        internal.getMessages()
                .getFaction()
                .getPlayer()
                .keySet()
                .stream()
                .filter(x -> !external
                        .getMessages()
                        .getFaction()
                        .getPlayer()
                        .containsKey(x))
                .forEach(x -> external
                        .getMessages()
                        .getFaction()
                        .getPlayer()
                        .put(x, internal
                                .getMessages()
                                .getFaction()
                                .getPlayer()
                                .get(x)));

        // Command messages
        internal.getMessages()
                .getCommand()
                .keySet()
                .stream()
                .filter(x -> !external
                        .getMessages()
                        .getCommand()
                        .containsKey(x))
                .forEach(x -> external
                        .getMessages()
                        .getCommand()
                        .put(x, internal
                                .getMessages()
                                .getCommand()
                                .get(x)));

        // Command messages
        internal.getMessages()
                .getCommand()
                .forEach((key, value) -> value.keySet()
                        .stream()
                        .filter(x -> !external
                                .getMessages()
                                .getCommand()
                                .get(key)
                                .containsKey(x))
                        .forEach(x -> external
                                .getMessages()
                                .getCommand()
                                .put(key, internal
                                        .getMessages()
                                        .getCommand()
                                        .get(key))));

    }
}
