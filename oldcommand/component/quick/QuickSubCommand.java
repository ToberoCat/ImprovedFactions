package io.github.toberocat.improvedFactions.core.oldcommand.component.quick;

import io.github.toberocat.improvedFactions.core.oldcommand.component.SubCommand;
import io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions.CommandExceptions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class QuickSubCommand extends SubCommand implements QuickCommand<QuickSubCommand> {

    private final QuickRun run;

    public QuickSubCommand(@NotNull String label) {
        super(label);
        this.run = (sender, args) -> false;
    }

    public QuickSubCommand(@NotNull String label, @NotNull QuickRun run) {
        super(label);
        this.run = run;
    }

    public QuickSubCommand(@NotNull String label, @NotNull QuickRunNoArgs run) {
        super(label);
        this.run = (player, args) -> {
            run.apply(player);
            return true;
        };
    }


    @Override
    protected boolean run(@NotNull CommandSender sender, @NotNull String[] args) throws CommandExceptions {
        return run.apply(sender, args);
    }

    @Override
    protected @Nullable List<String> runTab(@NotNull CommandSender sender, @NotNull String[] args) {
        return null;
    }

    @Override
    public @NotNull QuickSubCommand child(SubCommand command) {
        addChild(command);
        return this;
    }

    @FunctionalInterface
    public interface QuickRun {
        boolean apply(@NotNull CommandSender sender, @NotNull String[] args) throws CommandExceptions;
    }

    @FunctionalInterface
    public interface QuickRunNoArgs {
        void apply(@NotNull CommandSender player) throws CommandExceptions;
    }
}
