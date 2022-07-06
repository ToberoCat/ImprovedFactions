package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminPermanentSubCommand extends SubCommand {
    public AdminPermanentSubCommand() {
        super("permanent", "admin.permanent", "command.admin.permanent.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        faction.setPermanent(!faction.isPermanent());
        Language.sendRawMessage("The faction &e" +
                (!faction.isPermanent() ? "isn't permanent any more" : "is now permanent"), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Arrays.asList(DataAccess.listFilesFolder("Factions"));
    }
}
