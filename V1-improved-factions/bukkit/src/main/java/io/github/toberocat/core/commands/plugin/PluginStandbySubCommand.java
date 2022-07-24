package io.github.toberocat.core.commands.plugin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class PluginStandbySubCommand extends SubCommand {
    public PluginStandbySubCommand() {
        super("standby", "plugin.standby", "command.plugin.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Language.sendMessage("command.plugin.standby.success", player);
        MainIF.getIF().saveShutdown("&cUser " + player.getName() + " requested standby. Everything was working correctly");
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
