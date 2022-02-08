package io.github.toberocat.core.commands.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConfigSaveSubCommand extends SubCommand {
    public ConfigSaveSubCommand() {
        super("save", "config.save", LangMessage.COMMAND_CONFIG_SAVE_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        List<String> savedConfigs = MainIF.getIF().SaveConfigs();
        List<String> differences = new ArrayList<>(MainIF.getIF().getDataManagers().keySet());
        differences.removeAll(savedConfigs);

        for (String savedConfig : savedConfigs) {
            Language.sendMessage(LangMessage.COMMAND_CONFIG_SAVE_SUCCESS, player, new Parseable("{config}", savedConfig));
        }

        for (String backupConfig : differences) {
            Language.sendMessage(LangMessage.COMMAND_CONFIG_SAVE_BACKUP, player, new Parseable("{config}", backupConfig));

        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
