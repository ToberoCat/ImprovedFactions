package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.factions.rank.Rank;
import io.github.toberocat.core.utility.factions.rank.members.AdminRank;
import io.github.toberocat.core.utility.factions.rank.members.MemberRank;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinPrivateFactionSubCommand extends SubCommand {
    public JoinPrivateFactionSubCommand() {
        super("joinprivate", "admin.joinprivate", "", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtility.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        faction.join(player, Rank.fromString(AdminRank.registry));
        Language.sendRawMessage("Joined &e" + faction.getDisplayName(), player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return FactionUtility.getAllFactions();
    }
}
