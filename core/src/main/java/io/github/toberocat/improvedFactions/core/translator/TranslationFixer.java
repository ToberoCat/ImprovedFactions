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
                .stream()
                .filter(x -> !external
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .contains(x))
                .forEach(x -> external
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .add(x));

        // Players
        internal.getMessages()
                .getFaction()
                .getPlayer()
                .stream()
                .filter(x -> !external
                        .getMessages()
                        .getFaction()
                        .getPlayer()
                        .contains(x))
                .forEach(x -> external
                        .getMessages()
                        .getFaction()
                        .getPlayer()
                        .add(x));

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
