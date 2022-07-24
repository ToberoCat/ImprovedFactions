package io.github.toberocat.core.commands.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigReloadSubCommand extends SubCommand {
    public ConfigReloadSubCommand() {
        super("reload", "config.reload", "command.config.reload.description", false);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        for (Config config : MainIF.getIF().getConfigMap().values()) config.Reload();
        for (Extension extension : MainIF.LOADED_EXTENSIONS.values()) extension.reloadConfigs();

        Language.sendMessage("command.config.reload.success", player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }
}
