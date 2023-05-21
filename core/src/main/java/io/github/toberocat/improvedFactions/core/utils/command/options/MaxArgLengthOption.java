package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class MaxArgLengthOption implements Option {
    private final @NotNull Set<String> antiSpam;
    private final int maxLengthOfArg;
    private final int index;
    private final int spamEntryLifetime;


    public MaxArgLengthOption(int index, int maxLengthOfArg, int spamEntryLifetime) {
        this.maxLengthOfArg = maxLengthOfArg;
        this.index = index;
        this.antiSpam = new HashSet<>();
        this.spamEntryLifetime = spamEntryLifetime;
    }

    @Override
    public @NotNull String[] execute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (index >= args.length)
            return args;

        int length = args[index].length();
        if (length > maxLengthOfArg && !antiSpam.contains(sender.getName())) {
            addToSent(sender);
            throw new CommandException("exceptions.max-arg-length-exceeded", () -> new PlaceholderBuilder()
                    .placeholder("max-characters", maxLengthOfArg)
                    .placeholder("position", index)
                    .placeholder("provided", args[index].length())
                    .getPlaceholders());
        }

        if (length <= maxLengthOfArg && antiSpam.contains(sender.getName())) {
            removeFromSent(sender);
            throw new CommandException("exceptions.max-arg-length-bounded", () -> new PlaceholderBuilder()
                    .placeholder("max-characters", maxLengthOfArg)
                    .getPlaceholders());
        }

        return args;
    }

    private void addToSent(@NotNull CommandSender sender) {
        antiSpam.add(sender.getName());
        ImprovedFactions.api().getScheduler().runLater(() -> antiSpam.remove(sender.getName()),
                spamEntryLifetime);
    }

    private void removeFromSent(@NotNull CommandSender sender) {
        antiSpam.remove(sender.getName());
    }
}
