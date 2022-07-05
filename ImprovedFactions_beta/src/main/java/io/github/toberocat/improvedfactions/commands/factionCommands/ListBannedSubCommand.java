package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommandSettings;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ListBannedSubCommand extends SubCommand {
    public ListBannedSubCommand() {
        super("banList", LangMessage.BANNED_LIST_DESCRIPTION);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes).setFactionPermission(Faction.LIST_BANNED_PERMISSION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        PlayerData data = ImprovedFactionsMain.playerData.get(player.getUniqueId());
        for (UUID banned : data.playerFaction.getBannedPeople()) {
            OfflinePlayer bannedPlayer = Bukkit.getOfflinePlayer(banned);
            player.sendMessage(Language.getPrefix() + Language.format("&c" + bannedPlayer.getName()));
        }
        player.sendMessage(Language.getPrefix() + Language.format("&fAll banned people got displayed"));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
