package io.github.toberocat.core.utility.command;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class SubCommand {

    protected final ArrayList<SubCommand> subCommands;

    private static SubCommand lastSubCommand;

    protected enum CommandExecuteError { NoPermission, NoFaction, NotEnoughArgs, OtherError, PlayerNotFound, OnlyAdminCommand, NoFactionPermission, NoFactionNeed }

    protected final String subCommand;
    protected final String description;
    protected final boolean manager;
    protected final String permission;

    protected abstract void CommandExecute(Player player, String[] args);
    protected abstract List<String> CommandTab(Player player, String[] args);


    public SubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        this.subCommand = subCommand;
        this.permission = permission;
        this.description = descriptionKey;
        this.manager = manager;
        subCommands = new ArrayList<>();

        if (getSettings().isAllowAliases()) {
            MainIF.getConfigManager().getDataManager("commands.yml").getConfig().addDefault("commands." + permission + ".aliases", new ArrayList<String>());
            MainIF.getConfigManager().getDataManager("commands.yml").getConfig().addDefault("commands." + permission + ".costs", new ArrayList<String>());
            MainIF.getConfigManager().getDataManager("commands.yml").getConfig().options().copyDefaults(true);
            MainIF.getConfigManager().getDataManager("commands.yml").saveConfig();
        }
    }

    public SubCommand(String subCommand, String descriptionKey, boolean manager) {
        this.subCommand = subCommand;
        this.permission = subCommand;
        this.description = descriptionKey;
        this.manager = manager;
        subCommands = new ArrayList<>();

        if (getSettings().isAllowAliases()) {
            MainIF.getConfigManager().AddToDefaultConfig("commands." + permission + ".aliases", new ArrayList<String>(),"commands.yml");
            MainIF.getConfigManager().AddToDefaultConfig("commands." + permission + ".costs", 0, "commands.yml");
        }
    }

    protected String getExtendedDescription() {
        return "extended description";
    }

    protected String getUsage() {
        return "usage";
    }

    public static List<String> CallSubCommandsTab(List<SubCommand> subCommands, Player player, String[] args) {
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
                        List<String> str = command.CommandTab(player, newArguments);
                        if (str == null) {
                            str = new ArrayList<>();
                        }
                        List<String> subCommandStr = SubCommand.CallSubCommandsTab(command.subCommands, player, newArguments);
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

        if (!(Boolean) PlayerSettings.getSettings(player.getUniqueId()).getPaired().getPlayerSetting()
                .get("hideCommandDescription").getSelected() && results.size() == 1) {
            for (SubCommand command : subCommands) {
                if  (results.contains(command.getSubCommand())) {
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

    public SubCommandSettings getSettings() {
        return new SubCommandSettings();
    }

    public static AsyncCore<Boolean> CallSubCommands(String commandPath, List<SubCommand> subCommands, Player player, String[] args) {
        return AsyncCore.Run(() -> {
            if (args.length == 0) return false;
            for (SubCommand command : subCommands) {
                if (args[0].equalsIgnoreCase(command.getSubCommand()) || command.getAliases().contains(args[0])) {
                    String[] newArguments = Arrays.copyOfRange(args, 1, args.length);
                    command.CallSubCommand(player, newArguments);
                    if(!command.subCommands.isEmpty() && !CallSubCommands(commandPath + command.subCommand, command.subCommands, player, newArguments).await().getResult()) {
                        if (command.manager) {
                            AsyncCore.Run(() -> {
                                new PlayerMessageBuilder("&7Usage:&f Hover%Now your" +
                                        " hovering;{HOVER}% to see the description, click to execute.")
                                        .send(player);

                                for (SubCommand commandSub : command.subCommands) {
                                    String cmd = "/f" + commandPath + " " + command.subCommand + " " + commandSub.subCommand;
                                    new PlayerMessageBuilder("&e&l"+commandSub.subCommand + "%" +
                                            Language.getMessage(commandSub.description, player) + ";{HOVER}{CLICK(0)}% &r&7 - &8" + cmd,
                                            cmd).send(player);
                                }
                            });
                            return true;
                        }
                        Language.sendMessage(LangMessage.THIS_COMMAND_DOES_NOT_EXIST, player);
                    }

                    return true;
                }
            }
            return false;
        });
    }

    public void CallSubCommand(Player player, String[] args) {
        AsyncCore.Run(() -> {
            if (getSettings().canExecute(this, player, args, true)) {
                if (player != null) {
                    if (Debugger.hasPermission(player, "faction.commands." + permission)) {
                        if (MainIF.getEconomy() == null) {
                            CommandExecute(player, args);
                        } else {
                            EconomyResponse response = MainIF.getEconomy().withdrawPlayer(player, getCosts());

                            if (response.transactionSuccess()) {
                                if (response.amount != 0) {
                                    player.sendMessage(Language.getPrefix() + Language.format("&fYou paid &6" + response.amount + "&f for using this command. Your current balance is &a" + response.balance));
                                }
                                CommandExecute(player, args);
                            } else {
                                player.sendMessage(Language.getPrefix() + Language.format(response.errorMessage));
                            }
                        }
                    } else {
                        SendCommandExecuteError(CommandExecuteError.NoPermission, player);
                    }
                } else {
                    CommandExecute(null, args);
                }
            }
            return null;
        });
    }

    // * Callbacks
    public void SendCommandExecuteError(CommandExecuteError error, Player player) {
        switch (error) {
            case NoPermission -> player.sendMessage(Language.getPrefix() + "§cYou don't have enough permissions to use this command. Permission: faction.commands." + permission);
            case NoFaction -> player.sendMessage(Language.getPrefix() + "§cYou need to be in a faction to use this command");
            case NotEnoughArgs -> player.sendMessage(Language.getPrefix() + "§cThis command needs more arguments. Please check the usage if you don't know what arguments");
            case OtherError -> player.sendMessage(Language.getPrefix() + "§cAn error occurred while running the " + subCommand + " command");
            case PlayerNotFound -> player.sendMessage(Language.getPrefix() + "§cCoudn't find player");
            case OnlyAdminCommand -> player.sendMessage(Language.getPrefix() + "§cYou need admin rights to execute this command");
            case NoFactionPermission -> player.sendMessage(Language.getPrefix() + "§cYou don't have enough permissions to use this command. If you think you should be allowed, ask a faction admin");
            case NoFactionNeed -> player.sendMessage(Language.getPrefix() + "§cYou don't need to be in a faction to use this command");
        }
    }

    public void SendCommandExecuteError(Player player, String errorMessage) {
        Language.sendMessage(LangMessage.ERROR_GENERAL, player, new Parseable("{error}", errorMessage));
    }

    private List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        if (!MainIF.getConfigManager().containConfig("commands." + permission + ".aliases"))
            return aliases;

        aliases.addAll(MainIF.getConfigManager().getValue("commands." + permission + ".aliases"));

        return aliases;
    }

    private int getCosts() {
        return MainIF.getConfigManager().getValue("commands." + permission + ".costs");
    }

    protected boolean CommandDisplayCondition(Player player, String[] args) {
        if (Debugger.hasPermission(player, "faction.commands." + permission)) {
            return getSettings().canDisplay(this, player, args, false);
        }

        return false;
    }
    //? Getters and Setters

    public void AddCommand(SubCommand command) {
        subCommands.add(command);
    }

    public boolean RemoveCommand(String command) {
        Iterator<SubCommand> subs = subCommands.iterator();

        while(subs.hasNext()) {
            SubCommand cmd = subs.next();
            if (cmd.getSubCommand() == command) {
                subs.remove();
                return true;
            }
        }

        return false;
    }

    public ArrayList<SubCommand> getSubCommands() {
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
}
