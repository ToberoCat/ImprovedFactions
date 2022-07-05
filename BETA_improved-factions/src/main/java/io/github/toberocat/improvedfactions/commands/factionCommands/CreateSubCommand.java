package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.event.faction.FactionCreateEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Parseable;
import io.github.toberocat.improvedfactions.ranks.OwnerRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateSubCommand extends SubCommand {

    public CreateSubCommand() {
        super("create", LangMessage.CREATE_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (FactionUtils.getFaction(player) == null) {
            if (args.length == 1) {
                Faction faction = FactionUtils.getFactionByRegistry(ChatColor.stripColor(args[0]));

                if(faction == null) {
                    CreateFaction(player, args[0]);
                } else {
                    Language.sendMessage(LangMessage.CREATE_ALREADY_EXISTS, player);
                }
            } else
                Language.sendMessage(LangMessage.CREATE_NEED_NAME, player);
        } else
            Language.sendMessage(LangMessage.CREATE_ALREADY_IN_FACTION, player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<String>();
        if (args.length != 1) return arguments;

        if (player.hasPermission("faction.commands.create") && FactionUtils.getFaction(player) == null) {
            arguments.add("name");
            if (!player.hasPermission("faction.colors.colorInFactionName")
                && args[0].contains("&")) {
                Language.sendMessage(LangMessage.CREATE_NO_COLOR_IN_NAME_PERM, player);
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

    private void CreateFaction(Player player, final String _name) {
        String name = player.hasPermission("faction.colors.colorInFactionName")
                ? Language.format(_name) : _name;
        Faction faction = Faction.create(player, name);
        FactionCreateEvent createEvent = new FactionCreateEvent(faction, player);
        Bukkit.getPluginManager().callEvent(createEvent);
        if (!createEvent.isCancelled()) {
            faction.Join(player, Rank.fromString(OwnerRank.registry));
            Language.sendMessage(LangMessage.CREATE_SUCCESS, player,
                    new Parseable("{faction_displayname}", faction.getDisplayName()));
        } else {
            faction.DeleteFaction();
            Language.sendMessage(LangMessage.CREATE_CANNOT_JOIN, player,
                    new Parseable("{error}", createEvent.getCancelMessage()));
        }
    }
}
