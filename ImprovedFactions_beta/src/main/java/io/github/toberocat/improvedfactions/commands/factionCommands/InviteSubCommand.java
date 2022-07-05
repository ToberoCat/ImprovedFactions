package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.event.faction.FactionLeaveEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.language.Parseable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InviteSubCommand extends SubCommand {
    public InviteSubCommand() {
        super("invite", LangMessage.INVITE_DESCRIPTION);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 1) {
            invite(player, Bukkit.getPlayer(args[0]));
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
        }
    }

    public static void invite(Player player, Player playerToInvite) {
        Faction faction = FactionUtils.getFaction(player);

        if (faction.isFrozen()) {
            CommandExecuteError(CommandExecuteError.Frozen, player);
            return;
        }

        if (playerToInvite != null && playerToInvite.isOnline()) {
            Language.sendMessage(LangMessage.INVITE_SUCCESS_SENDER, player,
                    new Parseable("{player_receive}", playerToInvite.getDisplayName()));

            TextComponent textComponent = new TextComponent(Language.getPrefix() + Language.parse(
                    Language.getMessage(LangMessage.INVITE_SUCCESS_RECEIVER, playerToInvite),
                    new Parseable[] {
                            new Parseable("{faction_displayname}", faction.getDisplayName())
                    }));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f join" + JoinPrivateFactionSubCommand.joinUUID + " " + faction.getRegistryName()));
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Language.getMessage(LangMessage.INVITE_HOVER_EVENT, playerToInvite)).create()));
            ImprovedFactionsMain.playerData.get(playerToInvite.getUniqueId())
                    .invitations.add(faction.getRegistryName());
            playerToInvite.spigot().sendMessage(textComponent);
        } else {
            CommandExecuteError(CommandExecuteError.PlayerNotFound, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            for (Player on : Bukkit.getOnlinePlayers())
                arguments.add(on.getName());
        }
        return arguments;
    }

    @Override
    protected boolean CommandDisplayCondition(Player player, String[] args) {
        boolean result = super.CommandDisplayCondition(player, args);
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null)
            result = false;
        else if (!faction.hasPermission(player, Faction.INVITE_PERMISSION))
            result = false;
        return result;
    }
}
