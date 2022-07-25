package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class AdminFreezeSubCommand extends SubCommand {
    public AdminFreezeSubCommand() {
        super("freeze", "admin.freeze", "command.admin.freeze.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Faction<?> faction = null;
        try {
            faction = FactionHandler.getFaction(args[0]);
            faction.setFrozen(!faction.isFrozen());
        } catch (FactionNotInStorage e) {
            Language.sendRawMessage("&cCan't find given faction", player);
        }

        Language.sendRawMessage("The faction &e" +
                (!faction.isFrozen() ? "isn't frozen any more" : "is now frozen"), player);
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        List<String> ar = Arrays.asList(FileAccess.listFilesFolder("Factions"));
        ar.addAll(FactionHandler.getLoadedFactions().values().stream().map(x -> x.get).toList());

        return ar;
    }
}
