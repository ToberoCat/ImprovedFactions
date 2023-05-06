package io.github.toberocat.improvedFactions.core.utils.command;

import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.Option;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class SubCommand extends Command {

    protected final String label;
    protected final Option[] onCommandOptions;
    protected final Option[] onTabOptions;


    public SubCommand(@NotNull String label) {
        this(label, Options.getFromConfig(label));
    }

    public SubCommand(@NotNull String label, @NotNull Options options) {
        this(label, label, options);
    }
    public SubCommand(@NotNull String permission, @NotNull String label, @NotNull Options options) {
        this(permission, label, options.getCommandOptions(), options.getTabOptions());
    }

    public SubCommand(@NotNull String permission,
                      @NotNull String label,
                      Option[] onCommandOptions,
                      Option[] onTabOptions) {
        super(permission, label);
        this.label = label;
        this.onCommandOptions = onCommandOptions;
        this.onTabOptions = onTabOptions;
    }

    public boolean routeCall(@NotNull CommandSender sender,
                             @NotNull String[] args)
            throws CommandException {
        if (!sender.hasPermission(getPermission()))
            throw new CommandException("You don't have enough permissions to run this command");
        if (args.length == 0) return handleWithOptions(sender, args);


        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (newArgs.length >= 1 && children.containsKey(newArgs[0]))
            return children.get(newArgs[0]).routeCall(sender, newArgs);
        return handleWithOptions(sender, newArgs);
    }

    public @Nullable List<String> routeTab(@NotNull CommandSender sender,
                                           @NotNull String[] args) throws CommandException {
        if (!sender.hasPermission(getPermission()))
            return null;
        if (args.length == 0) return getTabWithOptions(sender, args);

        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, newArgs.length);

        if (newArgs.length >= 1 && children.containsKey(newArgs[0]))
            return children.get(newArgs[0]).routeTab(sender, newArgs);
        return getTabWithOptions(sender, newArgs);
    }

    protected void sendMessage(@NotNull CommandSender sender,
                               @NotNull String message,
                               Object... placeholders) {
        sender.sendMessage(prefix + String.format(MessageHandler.api().format(message), placeholders));
    }

    private boolean handleWithOptions(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        for (Option option : onCommandOptions)
            option.canExecute(sender, args);
        return handleCommand(sender, args);
    }

    private @Nullable List<String> getTabWithOptions(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        for (Option option : onCommandOptions)
            option.canExecute(sender, args);
        return getTabList(sender, args);
    }

    protected abstract boolean handleCommand(@NotNull CommandSender sender,
                                             @NotNull String[] args)
            throws CommandException;

    protected abstract @Nullable List<String> getTabList(@NotNull CommandSender sender,
                                                         @NotNull String[] args)
            throws CommandException;
}