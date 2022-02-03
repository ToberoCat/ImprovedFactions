package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

public class PluginDisableSubCommand extends SubCommand {
    public PluginDisableSubCommand() {
        super("disable", LangMessage.COMMAND_PLUGIN_DISABLE_DESCRIPTION, false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setCanUseInConsole(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (player != null) {
            Language.sendMessage(LangMessage.COMMAND_PLUGIN_DISABLE_SUCCESS, player);
        } else {
            MainIF.LogMessage(Level.INFO, Language.getMessage(LangMessage.COMMAND_PLUGIN_DISABLE_SUCCESS, "en_us"));
        }
        MainIF.getIF().getPluginLoader().disablePlugin(MainIF.getIF());
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
