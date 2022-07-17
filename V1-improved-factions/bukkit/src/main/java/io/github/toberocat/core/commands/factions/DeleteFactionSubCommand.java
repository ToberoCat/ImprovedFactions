package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.factions.local.rank.members.OwnerRank;
import io.github.toberocat.core.utility.command.ConfirmSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

public class DeleteFactionSubCommand extends ConfirmSubCommand {
    public DeleteFactionSubCommand() {
        super("delete", "command.faction.delete.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes).setRank(OwnerRank.registry);
    }

    @Override
    protected String confirmMessage(Player player) {
        return "command.faction.delete.confirm";
    }

    @Override
    protected void confirmExecute(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        faction.delete();

        Language.sendMessage("command.faction.delete.success", player);
    }
}
