package io.github.toberocat.core.commands.admin;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.migration.Migrator;
import io.github.toberocat.core.utility.command.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminMigrateCommand extends SubCommand {
    public AdminMigrateCommand() {
        super("migrate", "admin.migrate", "command.admin.migrate.discription", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        new Migrator(MainIF.getIF()).migrate();
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
