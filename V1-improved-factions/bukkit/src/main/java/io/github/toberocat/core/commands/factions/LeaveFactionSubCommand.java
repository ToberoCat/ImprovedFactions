package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.factions.local.rank.members.OwnerRank;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.ConfirmSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.player.PlayerSettings;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

public class LeaveFactionSubCommand extends ConfirmSubCommand {
    public LeaveFactionSubCommand() {
        super("leave", "command.leave.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected String confirmMessage(Player player) {
        return "command.faction.leave.confirm";
    }

    @Override
    protected void confirmExecute(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);

        if (!faction.isPermanent() && faction.getPlayerRank(player).getRegistryName().equals(OwnerRank.registry)) {
            Language.sendRawMessage("Can't leave your own faction. Delete it or transfer ownership", player);
            return;
        }

        Result result = faction.leave(player);
        if (!result.isSuccess()) {
            Language.sendRawMessage("Couldn't leave. " + result.getPlayerMessage(), player);
        } else {
            int timeout =  MainIF.getConfigManager().getDataManager("config.yml").getConfig()
                    .getInt("faction.joinTimeout");
            System.out.println(timeout);
            DateTime now = DateTime.now();
            now = now.plusHours(timeout);

            DateTimeFormatter fmt = DateCore.TIME_FORMAT;
            PlayerSettings.getSettings(player.getUniqueId()).getSetting("factionJoinTimeout").setSelected(fmt.print(now));
            Language.sendRawMessage("You left your faction. You can't join another until &6" + fmt.print(now), player);
        }
    }
}
