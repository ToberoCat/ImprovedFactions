package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ArgLengthOption implements Option {
    private final @NotNull Set<String> antiSpam;
    private final int length;
    private final int spamEntryLifetime;

    public ArgLengthOption(int length, int spamEntryLifetime) {
        this.length = length;
        this.antiSpam = new HashSet<>();
        this.spamEntryLifetime = spamEntryLifetime;
    }

    @Override
    public void canExecute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (args.length != length && !antiSpam.contains(sender.getName())) {
            addToSent(sender);
            throw new CommandException("exceptions.not-enough-arguments", () -> new PlaceholderBuilder()
                    .placeholder("provided", args.length)
                    .placeholder("required", length)
                    .getPlaceholders());
        }
    }

    private void addToSent(@NotNull CommandSender sender) {
        antiSpam.add(sender.getName());
        ImprovedFactions.api().getScheduler().runLater(() -> antiSpam.remove(sender.getName()),
                spamEntryLifetime);
    }
}
