package io.github.toberocat.improvedfactions.commands.subCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Debugger;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SubCommand {

    private static SubCommand lastSubCommand;

    protected enum CommandExecuteError { Frozen, NoPermission, NoFaction, NotEnoughArgs, OtherError, PlayerNotFound, OnlyAdminCommand, NoFactionPermission, NoFactionNeed };

    protected final String subCommand;
    protected final String permission;
    protected final String description;

    protected abstract void CommandExecute(Player player, String[] args);
    protected abstract List<String> CommandTab(Player player, String[] args);


    public SubCommand(String subCommand, String permission, String descriptionKey) {
        this.subCommand = subCommand;
        this.permission = permission;
        this.description = descriptionKey;

        if (getSettings().isAllowAliases()) {
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().addDefault("commands." + permission + ".aliases", new ArrayList<String>());
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().addDefault("commands." + permission + ".costs", new ArrayList<String>());
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().options().copyDefaults(true);
            ImprovedFactionsMain.getPlugin().getCommandData().saveConfig();
        }
    }

    public SubCommand(String subCommand, String descriptionKey) {
        this.subCommand = subCommand;
        this.permission = subCommand;
        this.description = descriptionKey;

        if (getSettings().isAllowAliases()) {
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().addDefault("commands." + permission + ".aliases", new ArrayList<String>());
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().addDefault("commands." + permission + ".costs", 0);
            ImprovedFactionsMain.getPlugin().getCommandData().getConfig().options().copyDefaults(true);
            ImprovedFactionsMain.getPlugin().getCommandData().saveConfig();
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
                        if (str != null)
                            arguments.addAll(str);
                    }
                }
            }
        }

        List<String> results = new ArrayList<String>();
        for (String arg : args) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(arg.toLowerCase())) {
                    results.add(a);
                }
            }
        }

        if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.commandDescriptions") && results.size() == 1) {
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

    public static boolean CallSubCommands(List<SubCommand> subCommands, Player player, String[] args) {
        if (args.length == 0) return false;
        for (SubCommand command : subCommands) {
            if (args[0].equalsIgnoreCase(command.getSubCommand()) || command.getAliases().contains(args[0])) {
                String[] newArguments = Arrays.copyOfRange(args, 1, args.length);
                command.CallSubCommand(player, newArguments);
                return true;
            }
        }
        return false;
    }

    public void CallSubCommand(Player player, String[] args) {
        if (player.hasPermission("faction.commands."+permission)) {
            if (getSettings().areConditionsTrue(this, player, args, true)) {
                Debugger.LogInfo("Calling command " + subCommand);
                if (ImprovedFactionsMain.getPlugin().getEconomy() == null) {
                    CommandExecute(player, args);
                } else {
                    if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.allowNegativeBalance") && ImprovedFactionsMain.getPlugin().getEconomy().getBalance(player) <= 0) {
                        player.sendMessage(Language.getPrefix() + Language.format("&cCan't withdraw " + getCosts() + ", because else you would have a negative balance"));
                        return;
                    }
                    EconomyResponse response = ImprovedFactionsMain.getPlugin().getEconomy().withdrawPlayer(player, getCosts());

                    if (response.transactionSuccess()) {
                        if (response.amount != 0) {
                            player.sendMessage(Language.getPrefix() + Language.format("&fYou paid &6" + response.amount + "&f for using this command. Your current balance is &a" + response.balance));
                        }
                        CommandExecute(player, args);
                    } else {
                        player.sendMessage(Language.getPrefix() + Language.format(response.errorMessage));
                    }
                }
            }
        } else {
            CommandExecuteError(CommandExecuteError.NoPermission, player);
        }
    }

    // * Callbacks
    public static void CommandExecuteError(CommandExecuteError error, Player player) {
        switch (error) {
            case NoPermission:
                player.sendMessage(Language.getPrefix() + "§cYou don't have enough permissions to use this command");
                break;
            case NoFaction:
                player.sendMessage(Language.getPrefix() + "§cYou need to be in a faction to use this command");
                break;
            case NotEnoughArgs:
                player.sendMessage(Language.getPrefix() + "§cThis command needs more arguments. Please check the usage if you don't know what arguments");
                break;
            case OtherError:
                player.sendMessage(Language.getPrefix() + "§cAn error occurred while running the command");
                break;
            case PlayerNotFound:
                player.sendMessage(Language.getPrefix() + "§cCoudn't find player");
                break;
            case OnlyAdminCommand:
                player.sendMessage(Language.getPrefix() + "§cYou need admin rights to execute this command");
                break;
            case NoFactionPermission:
                player.sendMessage(Language.getPrefix() + "§cYou don't have enough permissions to use this command. If you think you should be allowed, ask a faction admin");
                break;
            case NoFactionNeed:
                player.sendMessage(Language.getPrefix() + "§cYou don't need to be in a faction to use this command");
                break;
            case Frozen:
                Language.sendRawMessage("This action isn't allowed. Your faction got frozen. Please tell a admin if you think that's not right", player);
                break;
        }
    }

    private List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        if (!ImprovedFactionsMain.getPlugin().getCommandData().getConfig().contains("commands." + permission + ".aliases"))
            return aliases;

        aliases.addAll(ImprovedFactionsMain.getPlugin().getCommandData().getConfig()
                .getStringList("commands." + permission + ".aliases"));

        return aliases;
    }

    private int getCosts() {
        return ImprovedFactionsMain.getPlugin().getCommandData().getConfig()
                .getInt("commands." + permission + ".costs");
    }

    protected boolean CommandDisplayCondition(Player player, String[] args) {
        if (player.hasPermission("faction.commands." + permission)) {
            return getSettings().areConditionsTrue(this, player, args, false);
        }

        return false;
    }
    //? Getters and Setters
    public String getSubCommand() {
        return subCommand;
    }

    public String getPermission() {
        return permission;
    }
}
