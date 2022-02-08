package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaveFactionSubCommand extends SubCommand {
    public LeaveFactionSubCommand() {
        super("leave", "", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        Result result = faction.leave(player);
        if (!result.isSuccess()) {
            Language.sendRawMessage("Couldn't leave. " + result.getPlayerMessage(), player);
        } else {
            Language.sendRawMessage("You left your faction", player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
