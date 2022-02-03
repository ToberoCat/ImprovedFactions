package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginStandbySubCommand extends SubCommand  {
    public PluginStandbySubCommand() {
        super("standby", LangMessage.COMMAND_PLUGIN_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Language.sendMessage(LangMessage.COMMAND_PLUGIN_STANDBY_SUCCESS, player);
        MainIF.getIF().SaveShutdown("&cUser " + player.getName() + " requested standby. Everything was working correctly");
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
