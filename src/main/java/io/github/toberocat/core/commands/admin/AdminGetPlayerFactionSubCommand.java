package io.github.toberocat.core.commands.admin;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminGetPlayerFactionSubCommand extends SubCommand {
    public AdminGetPlayerFactionSubCommand() {
        super("playerfaction", "admin.playerfaction", "", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(1);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        String registry = FactionUtility.getPlayerFactionRegistry(player);
        if (registry == null) {
            Language.sendRawMessage("&6" + args[0] + "&f is in no faction", player);
            return;
        }
        Faction faction = FactionUtility.getFactionByRegistry(registry);
        if (faction == null) {
            Language.sendRawMessage("&6" + args[0] + "&f is in no faction", player);
        } else {
            Language.sendRawMessage("&6" + args[0] + "&f is in &e" + faction.getDisplayName(), player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
    }
}
