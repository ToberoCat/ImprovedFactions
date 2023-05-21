package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class JoinedLiteralOption implements Option {
    private final int maxLiteralLength;

    public JoinedLiteralOption() {
        this(-1);
    }

    public JoinedLiteralOption(int maxLiteralLength) {
        this.maxLiteralLength = maxLiteralLength;
    }

    @Override
    public @NotNull String[] execute(CommandSender sender, @NotNull String[] args)
            throws CommandException {
        int maxJoinLength = maxLiteralLength > 0 ? Math.min(args.length, maxLiteralLength) : args.length;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < maxJoinLength; i++)
            builder.append(args[i]).append(' ');
        String[] newArr = new String[args.length - maxJoinLength + 1];
        newArr[0] = builder.toString().trim();
        System.arraycopy(args, maxJoinLength, newArr, 1, newArr.length - 1);
        return newArr;
    }
}
