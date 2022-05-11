package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class PluginDisableSubCommand extends SubCommand {
    public PluginDisableSubCommand() {
        super("disable", "plugin.disable", "command.plugin.disable.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setCanUseInConsole(true).setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (player != null) {
            Language.sendMessage("command.plugin.disable.success", player);
        } else {
            MainIF.logMessage(Level.INFO, Language.getMessage("command.plugin.disable.success", "en_us"));
        }
        MainIF.getIF().getPluginLoader().disablePlugin(MainIF.getIF());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
