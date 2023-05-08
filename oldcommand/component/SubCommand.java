package io.github.toberocat.improvedFactions.core.oldcommand.component;

import io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions.CommandExceptions;
import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class SubCommand extends Command<SubCommand> {

    protected final String label;

    public SubCommand(@NotNull String label) {
        super(label, label);
        this.label = label;
    }

    public boolean routeCall(@NotNull CommandSender sender,
                             @NotNull String[] args)
            throws CommandExceptions {
        if (!sender.hasPermission(getPermission()))
            throw new CommandExceptions("You don't have enough permissions to run this command");
        if (args.length == 0) return run(sender, args);

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (newArgs.length >= 1 && children.containsKey(newArgs[0]))
            return children.get(newArgs[0]).routeCall(sender, newArgs);
        return run(sender, newArgs);
    }

    public @Nullable List<String> routeTab(@NotNull CommandSender sender,
                                           @NotNull String[] args) {
        if (!sender.hasPermission(getPermission()))
            return null;
        if (args.length == 0) return runTab(sender, args);

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (newArgs.length >= 1 && children.containsKey(newArgs[0]))
            return children.get(newArgs[0]).routeTab(sender, newArgs);
        return runTab(sender, newArgs);
    }

    protected void sendMessage(@NotNull CommandSender sender,
                               @NotNull String message,
                               Object... placeholders) {
        sender.sendMessage(String.format(message, placeholders));
    }

    protected @NotNull ConfigHandler getConfig() {
        return ImprovedFactions.api().getConfig("commands.yml").getSection(label);
    }

    protected abstract boolean run(@NotNull CommandSender sender,
                                   @NotNull String[] args)
            throws CommandExceptions;

    protected abstract @Nullable List<String> runTab(@NotNull CommandSender sender,
                                                     @NotNull String[] args);
}
