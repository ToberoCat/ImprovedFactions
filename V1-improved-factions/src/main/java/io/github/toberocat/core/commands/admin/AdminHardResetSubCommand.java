package io.github.toberocat.core.commands.admin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

public class AdminHardResetSubCommand extends SubCommand {
    public AdminHardResetSubCommand() {
        super("reset", "admin.reset", "command.admin.hardreset.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 0) {
            Language.sendMessage("command.admin.hardreset.confirm_player", player);
        } else if (args[0].equals("confirm")) {
            MainIF.getIF().getClaimManager().CLAIMS.forEach((key, value) -> {
                World world = Bukkit.getWorld(key);
                value.forEach((claim) -> {
                    PersistentDataUtility.remove(PersistentDataUtility.FACTION_CLAIMED_KEY,
                            world.getChunkAt(claim.getX(), claim.getY()).getPersistentDataContainer());
                });
            });

            DataAccess.reset();

            String path = MainIF.getIF().getDataFolder().getPath() + "/";
            new File(path + "config.yml").delete();
            new File(path + "commands.yml").delete();
            for (File file : new File(path + "lang").listFiles()) {
                file.delete();
            }

            PlayerSettings.getLoadedSettings().clear();

            MainIF.getIF().getSaveEvents().clear();
            MainIF.logMessage(Level.SEVERE, "&cImprovedFactions got a reset. Please reload / restart the server");
            Language.sendMessage("command.admin.hardreset.success", player);
            MainIF.getIF().getPluginLoader().disablePlugin(MainIF.getIF());
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
