package io.github.toberocat.core.utility.command;

import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ConfirmSubCommand extends SubCommand {
    public ConfirmSubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        super(subCommand, permission, descriptionKey, manager);
    }

    public ConfirmSubCommand(String subCommand, String descriptionKey, boolean manager) {
        super(subCommand, descriptionKey, manager);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings();
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length >= 1 && args[0].equals("confirm")) confirmExecute(player);
        else Language.sendMessage(confirmMessage(player), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return List.of("confirm");
    }

    protected abstract String confirmMessage(Player player);

    protected abstract void confirmExecute(Player player);
}
