package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public abstract class ConfirmCommand<P extends Command.CommandPacket, C extends Command.ConsoleCommandPacket>
        extends Command<P, C> {

    @Override
    public final @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        if (args.length <= 1) return Collections.emptyList();
        return tabCompleteConfirmed(player, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public final @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return Collections.emptyList();
        return tabCompleteConfirmed(Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public final @Nullable P createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        if (args.length < 1) {
            executor.sendMessage(notConfirmedTranslatable());
            return null;
        }

        return createFromArgsConfirmed(executor, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public final @Nullable C createFromArgs(@NotNull String[] args) {
        if (args.length < 1) {
            Logger.api().logInfo("Command isn't confirmed");
            return null;
        }

        return createFromArgsConfirmed(Arrays.copyOfRange(args, 1, args.length));
    }

    protected abstract @NotNull List<String> tabCompleteConfirmed(@NotNull FactionPlayer<?> player,
                                                                  @NotNull String[] args);

    protected abstract @NotNull List<String> tabCompleteConfirmed(@NotNull String[] args);

    protected abstract @Nullable P createFromArgsConfirmed(@NotNull FactionPlayer<?> executor,
                                                           @NotNull String[] args);

    protected abstract @Nullable C createFromArgsConfirmed(@NotNull String[] args);

    protected abstract Function<Translatable, String> notConfirmedTranslatable();
}
