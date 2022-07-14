package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.factions.local.rank.members.MemberRank;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.entity.Player;
import org.joda.time.Period;

import java.util.List;

public class JoinFactionSubCommand extends SubCommand {
    public JoinFactionSubCommand() {
        super("join", "command.join.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1).setNeedsFaction(SubCommandSettings.NYI.No);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        LocalFaction faction = FactionUtility.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        if (faction.getOpenType() == LocalFaction.OpenType.CLOSED) {
            Language.sendRawMessage("&cGiven faction is private", player);
            return;
        }

        if (!DateCore.hasTimeout(player)) {
            Result result = faction.join(player, Rank.fromString(MemberRank.registry));
            if (result.isSuccess()) {
                Language.sendRawMessage("Joined &e" + faction.getDisplayName(), player);
            } else {
                Language.sendRawMessage(result.getPlayerMessage(), player);
            }
        } else {
            Period difference = DateCore.leftTimeDifference(player);
            Parser.run("command.faction.join.in-timeout")
                    .parse("{time}", DateCore.formatPeriod(difference))
                    .send(player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return FactionUtility.getAllFactions();
    }
}
