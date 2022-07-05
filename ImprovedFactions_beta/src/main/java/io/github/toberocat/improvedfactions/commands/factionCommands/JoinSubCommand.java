package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.event.faction.FactionJoinEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.gui.FlagUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Parseable;
import io.github.toberocat.improvedfactions.ranks.MemberRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class JoinSubCommand extends SubCommand {
    public JoinSubCommand() {
        super("join", LangMessage.JOIN_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) == null) {
            if (args.length != 1) {
                CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
                return;
            }
            Faction foundFaction = FactionUtils.getFactionByRegistry(args[0]);
            if (foundFaction == null)
            {
                Language.sendMessage(LangMessage.JOIN_ERROR_NO_FACTION_FOUND, player);
                return;
            }

            if (!FlagUtils.CompareEnum(foundFaction.getSettings().getFlags().get(Faction.OPENTYPE_FLAG), Faction.OpenType.Public)) {
                Language.sendMessage(LangMessage.JOIN_ERROR_FACTION_PRIVATE, player);
                return;
            }

            if (foundFaction.getBannedPeople().contains(player.getUniqueId())) {
                Language.sendMessage(LangMessage.JOIN_ERROR_FACTION_BANNED, player);
                return;
            }

            if (foundFaction.isFrozen()) {
                CommandExecuteError(CommandExecuteError.Frozen, player);
                return;
            }

            if (foundFaction.Join(player, Rank.fromString(MemberRank.registry))) {
                Language.sendMessage(LangMessage.JOIN_SUCCESS, player,
                        new Parseable("{faction_displayname}", foundFaction.getDisplayName()));
            } else {
                if (foundFaction.hasMaxMembers())
                    Language.sendMessage(LangMessage.JOIN_FULL, player);
            }
        } else {
            player.sendMessage(Language.getPrefix() + "§cYou have already joined a faction. Please leave before joining another faction");
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length != 1) return arguments;
        for (Faction faction : Faction.getFACTIONS()) {
            if (FlagUtils.CompareEnum(faction.getSettings().getFlags().get(Faction.OPENTYPE_FLAG), Faction.OpenType.Public)) {
                arguments.add(ChatColor.stripColor(faction.getDisplayName()));
            }
        }
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        if (FactionUtils.getFaction(player) != null)
            result = false;
        return result;
    }
}