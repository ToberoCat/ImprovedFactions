package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdminFreezeSubCommand extends SubCommand {
    public AdminFreezeSubCommand() {
        super("freeze", "admin.freeze",
                "command.admin.freeze.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        try {
            Faction<?> faction = FactionHandler.getFaction(args[0]);
            faction.setFrozen(!faction.isFrozen());
            Language.sendMessage(key(faction), player, new Parseable("{faction}", faction.getDisplay()));
        } catch (FactionNotInStorage e) {
            Language.sendRawMessage("&cCan't find given faction", player);
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return FactionHandler.getAllFactions().toList();
    }

    protected @NotNull String key(@NotNull Faction<?> faction) {
        return faction.isFrozen() ? "command.admin.freeze.frozen" :
                "command.admin.freeze.unfrozen";
    }
}
