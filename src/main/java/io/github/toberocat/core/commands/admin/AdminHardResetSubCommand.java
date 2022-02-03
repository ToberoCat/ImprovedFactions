package io.github.toberocat.core.commands.admin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;

public class AdminHardResetSubCommand extends SubCommand {
    public AdminHardResetSubCommand() {
        super("reset", LangMessage.COMMAND_ADMIN_HARD_RESET_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 0) {
            Language.sendMessage(LangMessage.COMMAND_ADMIN_HARD_RESET_CONFIRM_PLAYER, player);
        } else if (args[0].equals("confirm")) {
            DataAccess.clearFolder("Factions");
            DataAccess.clearFolder("Chunks");
            DataAccess.clearFolder("History");
            DataAccess.clearFolder("Players");

            String path = MainIF.getIF().getDataFolder().getPath() + "/";
            new File(path + "config.yml").delete();
            new File(path + "commands.yml").delete();
            for (File file : new File(path + "lang").listFiles()) {
                file.delete();
            }

            PlayerSettings.getLoadedSettings().clear();

            MainIF.getIF().getSaveEvents().clear();
            MainIF.LogMessage(Level.SEVERE, "&cImprovedFactions got a reset. Please reload / restart the server");
            Language.sendMessage(LangMessage.COMMAND_ADMIN_HARD_RESET_SUCCESS, player);
            MainIF.getIF().getPluginLoader().disablePlugin(MainIF.getIF());
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
