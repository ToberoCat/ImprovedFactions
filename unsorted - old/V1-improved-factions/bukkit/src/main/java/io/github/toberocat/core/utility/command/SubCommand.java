package io.github.toberocat.core.utility.command;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.player.PlayerSettingHandler;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class SubCommand {

    private static SubCommand lastSubCommand;
    protected final LinkedHashSet<SubCommand> subCommands;
    protected final String subCommand;
    protected final String description;
    protected final boolean manager;
    protected final String permission;

    private final DataManager commands;

    public SubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        this.subCommand = subCommand;
        this.permission = permission;
        this.description = descriptionKey;
        this.manager = manager;
        this.subCommands = new LinkedHashSet<>();
        this.commands = new DataManager(MainIF.getIF(), "commands.yml");

        createDefault();
    }

    public SubCommand(String subCommand, String descriptionKey, boolean manager) {
        this(subCommand, subCommand, descriptionKey, manager);
    }

    public static List<String> callTabComplete(HashSet<SubCommand> subCommands, Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) { //Means: The first subcommand is determined
            for (SubCommand command : subCommands) {
                String[] newArguments = Arrays.copyOfRange(args, 1, args.length);
                if (command.CommandDisplayCondition(player, newArguments)) {
                    arguments.add(command.subCommand);
                    arguments.addAll(command.getAliases());
                }
            }
        } else {
            for (SubCommand command : subCommands) {
                if (args[0].equalsIgnoreCase(command.getSubCommand()) || command.getAliases().contains(args[0])) {
                    String[] newArguments = Arrays.copyOfRange(args, 1, args.length);
                    if (command.CommandDisplayCondition(player, newArguments)) {
                        List<String> str = command.commandTab(player, newArguments);
                        if (str == null) {
                            str = new ArrayList<>();
                        }
                        List<String> subCommandStr = SubCommand.callTabComplete(command.subCommands, player, newArguments);
                        if (subCommandStr != null) {
                            str.addAll(subCommandStr);
                        }
                        arguments.addAll(str);
                    }
                }
            }
        }

        List<String> results = new ArrayList<>();
        for (String arg : args) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(arg.toLowerCase())) {
                    results.add(a);
                }
            }
        }

        if (!(Boolean) PlayerSettingHandler.getSettings(player.getUniqueId())
                .getT("hideCommandDescription").getSelected() && results.size() == 1) {
            for (SubCommand command : subCommands) {
                if (results.contains(command.getSubCommand())) {
                    if (lastSubCommand != command) {
                        Language.sendMessage(command.description, player);
                        lastSubCommand = command;
                    }
                    break;
                }
            }
        }
        if (arguments.isEmpty())
            return null;
        return arguments;
    }

    public static AsyncTask<Boolean> callSubCommands(String commandPath, HashSet<SubCommand> subCommands, Player player, String[] args) {
        return AsyncTask.run(() -> {
            if (args.length == 0) return false;
            for (SubCommand command : subCommands) {
                if (args[0].equalsIgnoreCase(command.getSubCommand()) || command.getAliases().contains(args[0])) {
                    String[] newArguments = Arrays.copyOfRange(args, 1, args.length);
                    command.callSubCommand(player, newArguments);
                    if (!command.subCommands.isEmpty() && !callSubCommands(commandPath + command.subCommand, command.subCommands, player, newArguments).await()) {
                        if (command.manager) {
                            AsyncTask.run(() -> {
                                new PlayerMessageBuilder("&7Usage:&f Hover%Now your" +
                                        " hovering;{HOVER}% to see the description, click to execute.")
                                        .send(player);

                                for (SubCommand commandSub : command.subCommands) {
                                    String cmd = "/f" + commandPath + " " + command.subCommand + " " + commandSub.subCommand;
                                    new PlayerMessageBuilder("&e&l" + commandSub.subCommand + "%" +
                                            Language.getMessage(commandSub.description, player) + ";{HOVER}{CLICK(0)}% &r&7 - &8" + cmd,
                                            cmd).send(player);
                                }
                            });
                            return true;
                        }
                        Language.sendMessage("command.not-exist", player);
                    }

                    return true;
                }
            }
            return false;
        });
    }

    private void createDefault() {
        if (!getSettings().isAllowAliases()) return;

        FileConfiguration config = commands.getConfig();
        config.addDefault("commands." + permission + ".aliases", new ArrayList<String>());
        config.addDefault("commands." + permission + ".costs", 0);
        config.options().copyDefaults(true);
        commands.saveConfig();
    }

    protected abstract void commandExecute(Player player, String[] args);

    protected abstract List<String> commandTab(Player player, String[] args);

    protected String getExtendedDescription() {
        return "extended description";
    }

    protected String getUsage() {
        return "usage";
    }

    public SubCommandSettings getSettings() {
        return new SubCommandSettings();
    }

    public void callSubCommand(Player player, String[] args) {
        AsyncTask.run(() -> {
            if (getSettings().canExecute(this, player, args, true)) {
                if (player != null) {
                    if (Debugger.hasPermission(player, "faction.commands." + permission)) {
                        if (MainIF.getEconomy() == null) {
                            commandExecute(player, args);
                        } else {
                            EconomyResponse response = MainIF.getEconomy().withdrawPlayer(player, getCosts());

                            if (response.transactionSuccess()) {
                                if (response.amount != 0) {
                                    player.sendMessage(Language.getPrefix(player) + Language.format("&fYou paid &6" + response.amount + "&f for using this command. Your current balance is &a" + response.balance));
                                }
                                commandExecute(player, args);
                            } else {
                                player.sendMessage(Language.getPrefix(player) + Language.format(response.errorMessage));
                            }
                        }
                    } else {
                        sendCommandExecuteError(CommandExecuteError.NoPermission, player);
                    }
                } else {
                    commandExecute(null, args);
                }
            }
            return null;
        });
    }

    // Callbacks
    public void sendCommandExecuteError(CommandExecuteError error, Player player) {
        switch (error) {
            case NoPermission -> player.sendMessage(Language.getPrefix(player) + "§cYou don't have enough permissions to use this command. Permission: faction.commands." + permission);
            case NoFaction -> player.sendMessage(Language.getPrefix(player) + "§cYou need to be in a faction to use this command");
            case ToLessArgs -> Language.sendRawMessage("&cThis command needs more arguments", player);
            case OtherError -> player.sendMessage(Language.getPrefix(player) + "§cAn error occurred while running the " + subCommand + " command");
            case PlayerNotFound -> player.sendMessage(Language.getPrefix(player) + "§cCouldn't find player");
            case OnlyAdminCommand -> player.sendMessage(Language.getPrefix(player) + "§cYou need admin rights to execute this command");
            case NoFactionPermission -> player.sendMessage(Language.getPrefix(player) + "§cYou don't have enough permissions to use this command. If you think you should be allowed, ask a faction admin");
            case NoFactionNeed -> Language.sendRawMessage("§cYou don't need to be in a faction to use this command", player);
            case ToManyArgs -> Language.sendRawMessage("&cYou gave too many arguments", player);
        }
    }

    public void sendCommandExecuteError(String errorMessage, Player player) {
        Language.sendMessage("error.general", player, new Parseable("{error}", errorMessage));
    }

    private List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        if (!commands.getConfig().contains("commands." + permission + ".aliases"))
            return aliases;

        return commands.getConfig().getStringList("commands." + permission + ".aliases");
    }

    private int getCosts() {
        if (!commands.getConfig().contains("commands." + permission + ".costs") &&
                commands.getConfig().get("commands." + permission + ".costs") != null) {
            return 0;
        }

        return commands.getConfig().getInt("commands." + permission + ".costs");
    }

    protected boolean CommandDisplayCondition(Player player, String[] args) {
        if (Debugger.hasPermission(player, "faction.commands." + permission)) {
            return getSettings().canDisplay(this, player, args, false);
        }

        return false;
    }

    public void addCommand(SubCommand command) {
        subCommands.add(command);
    }
    //? Getters and Setters

    public boolean removeCommand(String command) {
        Iterator<SubCommand> subs = subCommands.iterator();

        while (subs.hasNext()) {
            SubCommand cmd = subs.next();
            if (Objects.equals(cmd.getSubCommand(), command)) {
                subs.remove();
                return true;
            }
        }

        return false;
    }

    public LinkedHashSet<SubCommand> getSubCommands() {
        return subCommands;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    protected enum CommandExecuteError {
        NoPermission,
        NoFaction,
        ToLessArgs,
        ToManyArgs,
        OtherError,
        PlayerNotFound,
        OnlyAdminCommand,
        NoFactionPermission,
        NoFactionNeed
    }
}
