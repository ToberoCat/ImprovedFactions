package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminDisbandSubCommand extends SubCommand  {
    public AdminDisbandSubCommand() {
        super("disband", LangMessage.COMMAND_ADMIN_DISBAND_DESCRIPTION, false);
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
        faction.delete();
        Language.sendMessage(LangMessage.COMMAND_ADMIN_DISBAND_SUCCESS, player,
                new Parseable("{faction_display}", faction.getDisplayName()));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return FactionUtility.getAllFactions();
    }
}
