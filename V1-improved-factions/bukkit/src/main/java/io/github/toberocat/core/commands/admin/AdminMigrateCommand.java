package io.github.toberocat.core.commands.admin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class AdminMigrateCommand extends SubCommand {
    public AdminMigrateCommand() {
        super("migrate", "admin.migrate", "command.admin.migrate.description", false);
    }


    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (!new File(MainIF.getIF().getDataFolder().getPath() + "/Data/factions.yml").exists() ||
                !new File(MainIF.getIF().getDataFolder().getPath() + "/Data/chunkData.yml").exists()) {
            Language.sendRawMessage("Couldn't find migratable files", player);
            return;
        }
        Language.sendRawMessage("Migration will now run on a separate thread. Please don't stop the server until you received the message that migration finished", player);
        AsyncTask.run(() -> {
            LocalFaction.migrateFaction();
            ClaimManager.migrate();
            Language.sendRawMessage("Migrated old beta data into the new format. Please check the console for any errors", player);
        });
    }

    private UUID getFromFactionMember(String str) {
        UUID uuid = null;
        String[] parms = str.split("[,=]");
        for (int i = 0; i < parms.length; i++) {
            String parm = parms[i];
            if (parm.contains("uuid")) {
                uuid = UUID.fromString(parms[i + 1].replace("}", ""));
            }
        }

        return uuid;
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
