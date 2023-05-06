package io.github.toberocat.improvedFactions.core.oldcommand.component.quick;

import io.github.toberocat.improvedFactions.core.oldcommand.component.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.oldcommand.component.SubCommand;
import io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions.CommandExceptions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class QuickPlayerSubCommand extends PlayerSubCommand implements QuickCommand<QuickPlayerSubCommand> {

    private final QuickPlayerRun run;

    public QuickPlayerSubCommand(@NotNull String label) {
        this(label, player -> {});
    }

    public QuickPlayerSubCommand(@NotNull String label,
                                 @NotNull QuickPlayerRunNoArgs run) {
        this(label, (u, args) -> {
            run.apply(u);
            return true;
        });
    }

    public QuickPlayerSubCommand(@NotNull String label,
                                 @NotNull QuickPlayerRun run) {
        super(label);
        this.run = run;
    }

    @Override
    public @NotNull QuickPlayerSubCommand child(SubCommand command) {
        addChild(command);
        return this;
    }

    @Override
    protected boolean runPlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args)
            throws CommandExceptions {
        return run.apply(player, args);
    }

    @Override
    protected @Nullable List<String> runPlayerTab(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return null;
    }

    @FunctionalInterface
    public interface QuickPlayerRun {
        boolean apply(@NotNull FactionPlayer<?> player, @NotNull String[] args) throws CommandExceptions;
    }

    @FunctionalInterface
    public interface QuickPlayerRunNoArgs {
        void apply(@NotNull FactionPlayer<?> player) throws CommandExceptions;
    }
}
