package io.github.toberocat.core.commands.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class ConfigRemoveAllBackupSubCommand extends SubCommand {
    public ConfigRemoveAllBackupSubCommand() {
        super("removeBackups", "config.removeBackups", LangMessage.COMMAND_CONFIG_BACKUP_REMOVE_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        MainIF.getIF().getBackupFile().clear();
        Language.sendMessage(LangMessage.COMMAND_CONFIG_BACKUP_REMOVE_SUCCESS, player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        return super.CommandDisplayCondition(player, args) && !MainIF.getIF().getBackupFile().isEmpty();
    }

}
