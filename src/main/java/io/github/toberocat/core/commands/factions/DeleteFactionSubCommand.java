package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.rank.OwnerRank;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class DeleteFactionSubCommand extends SubCommand {
    public DeleteFactionSubCommand() {
        super("delete", LangMessage.COMMAND_FACTION_DELETE_DESCRIPTION, false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes).setRank(OwnerRank.registry);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        faction.delete();

        Language.sendMessage(LangMessage.COMMAND_FACTION_DELETE_SUCCESS, player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
