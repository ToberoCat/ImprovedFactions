package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LangMessageAction extends Action {
    @Override
    public @NotNull String label() {
        return "lang-message";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args) {
        String translatable = args[0];
        Language.sendMessage(translatable, player, Arrays.stream(args)
                .skip(1)
                .map(this::fromParse)
                .toArray(Parseable[]::new));
    }

    private Parseable fromParse(@NotNull String parse) {
        String[] parts = parse.split(":");
        String from = parts[0];
        String to = parts[1];

        return new Parseable(from, to);
    }
}
