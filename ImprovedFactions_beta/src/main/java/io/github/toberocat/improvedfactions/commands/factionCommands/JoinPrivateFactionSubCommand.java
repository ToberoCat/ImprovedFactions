package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.event.faction.FactionJoinEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Parseable;
import io.github.toberocat.improvedfactions.ranks.NewMemberRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinPrivateFactionSubCommand extends SubCommand {
    public static UUID joinUUID = UUID.randomUUID();
    public JoinPrivateFactionSubCommand() {
        super("join" + joinUUID.toString(), "");
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setAllowAliases(false).setNeedsFaction(SubCommandSettings.NYI.No);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length != 1) {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
            return;
        }
        Faction faction = FactionUtils.getFactionByRegistry(args[0]);
        if (faction.isFrozen()) {
            CommandExecuteError(CommandExecuteError.Frozen, player);
            return;
        }
        JoinPrivate(player, args[0]);
    }

    public static void JoinPrivate(Player player, String faction) {
        if (FactionUtils.getFaction(player) == null) {
            Faction foundFaction = FactionUtils.getFactionByRegistry(faction);
            if (foundFaction == null)
            {
                Language.sendMessage(LangMessage.JOIN_ERROR_NO_FACTION_FOUND, player);
                return;
            }

            if (foundFaction.getBannedPeople().contains(player.getUniqueId())) {
                Language.sendMessage(LangMessage.JOIN_ERROR_FACTION_BANNED, player);
                return;
            }

            if (foundFaction.Join(player, Rank.fromString(NewMemberRank.registry))) {
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
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        return false;
    }

    @Override
    public void CallSubCommand(Player player, String[] args) {
        CommandExecute(player, args);
    }
}
