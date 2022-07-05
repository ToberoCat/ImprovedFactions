package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UnbanSubCommand extends SubCommand {
    public UnbanSubCommand() {
        super("unban", "unban", LangMessage.UNBAN_DESCRIPTION);
    }

    @Override
    public SubCommandSettings getSettings() {
        return new SubCommandSettings().setNeedsAdmin(true).setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Faction faction = FactionUtils.getFaction(player);
        if (args.length == 1) {
            Player unbanned = Bukkit.getPlayer(args[0]);
            if (unbanned == null) {
                CommandExecuteError(CommandExecuteError.PlayerNotFound, player);
                return;
            }
            UUID uuid = unbanned.getUniqueId();
            if (faction.getBannedPeople().contains(uuid)) {
                faction.getBannedPeople().remove(uuid);
                player.sendMessage(Language.getPrefix() + Language.format("Banned &e" + args[0]));
                faction.Leave(player);
            } else {
                player.sendMessage(Language.getPrefix() + Language.format("&cCannot unban not banned"));
            }
        } else {
            CommandExecuteError(CommandExecuteError.NotEnoughArgs, player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (args.length == 1) {
            PlayerData data = ImprovedFactionsMain.playerData.get(player.getUniqueId());
            if (data.playerFaction != null) {
                for (UUID uuid : data.playerFaction.getBannedPeople()) {
                    arguments.add(Bukkit.getOfflinePlayer(uuid).getName());
                }
            }
        }
        return arguments;
    }
}
