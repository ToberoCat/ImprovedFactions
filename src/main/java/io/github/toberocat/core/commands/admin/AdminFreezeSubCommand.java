package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminFreezeSubCommand extends SubCommand {
    public AdminFreezeSubCommand() {
        super("freeze", "admin.freeze", "", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        faction.setFrozen(!faction.isFrozen());
        Language.sendRawMessage("The faction &e" +
                (!faction.isFrozen() ? "isn't frozen any more" : "is now frozen") , player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> ar = Arrays.asList(DataAccess.listFiles("Factions"));
        ar.addAll(Faction.getLoadedFactions().values().stream().map(Faction::getRegistryName).toList());

        return ar;
    }
}
