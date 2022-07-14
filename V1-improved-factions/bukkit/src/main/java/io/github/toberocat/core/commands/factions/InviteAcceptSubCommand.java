package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.factions.local.rank.members.MemberRank;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class InviteAcceptSubCommand extends SubCommand {
    public InviteAcceptSubCommand() {
        super("inviteaccept", "command.invite-accept.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.No).setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        LocalFaction faction = FactionUtility.getFactionByRegistry(args[0]);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        if (AsyncTask.find(LocalFaction.getLoadedFactions().values(),
                x -> x.getFactionMemberManager().getInvitations().contains(player.getUniqueId())) == null) {
            Language.sendMessage("command.faction.invite-accept.failed", player);
            return;
        }

        Result result = faction.join(player, Rank.fromString(MemberRank.registry));
        if (result.isSuccess()) {
            Language.sendRawMessage("Joined &e" + faction.getDisplayName(), player);
        } else {
            Language.sendRawMessage(result.getPlayerMessage(), player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return LocalFaction.getLoadedFactions().values().stream().filter(x -> x.getFactionMemberManager()
                .getInvitations().contains(player.getUniqueId())).map(LocalFaction::getRegistryName).toList();
    }
}
