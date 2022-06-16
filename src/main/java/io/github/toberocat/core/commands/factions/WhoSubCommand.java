package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class WhoSubCommand extends SubCommand {
    public WhoSubCommand() {
        super("who", "command.who.descriptions", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        String factionRegistry;
        if (args.length == 0) {
            if (!FactionUtility.isInFaction(player)) {
                Language.sendRawMessage("&cYou are in no faction. Please select one", player);
                return;
            }
            factionRegistry = FactionUtility.getPlayerFactionRegistry(player);
        } else {
            factionRegistry = args[0];
        }
        if (factionRegistry == null) {
            sendCommandExecuteError(CommandExecuteError.NoFaction, player);
            return;
        }

        Faction faction = FactionUtility.getFactionByRegistry(factionRegistry);
        if (faction == null) {
            Language.sendRawMessage("&cCan't find given faction", player);
            return;
        }

        String displayName = faction.getDisplayName();

        String topBottomMessage = "=".repeat(displayName.length() + 10);
        Language.sendRawMessage("&f" + topBottomMessage, player);
        Language.sendRawMessage("&f====  &e" + displayName + "&f  ====", player);
        Language.sendRawMessage("&f" + topBottomMessage, player);
        Language.sendRawMessage("Motd: &e" + faction.getMotd(), player);

        for (String description : faction.getDescription()) {
            Language.sendRawMessage("Description: " + description, player);
        }
        Language.sendRawMessage("Registry: &e" + factionRegistry, player);


        Language.sendRawMessage("Owner: &e" + Bukkit.getOfflinePlayer(faction.getOwner()).getName(), player);

        Language.sendRawMessage("Members online: " +
                faction.getFactionMemberManager().getOnlinePlayers().size() + "/" +
                faction.getFactionMemberManager().getMembers().size(), player);

        Language.sendRawMessage("Power: " +
                faction.getPowerManager().getCurrentPower() + "/" +
                faction.getPowerManager().getMaxPower(), player);

        Language.sendRawMessage("Chunk claim: " +
                (faction.getPowerManager().overclaimable() ? "&f" : "&c") +
                faction.getClaimedChunks() + "/" +
                faction.getPowerManager().getCurrentPower(), player);

        Language.sendRawMessage("Wars: &7" + faction.getRelationManager().getEnemies().size(), player);

        Language.sendRawMessage("Ally: &7" + faction.getRelationManager().getAllies().size(), player);

        Language.sendRawMessage("Banned players: &7"
                + faction.getFactionMemberManager().getBanned().size(), player);

        if (faction.getFactionBank().balance() == null) {
            Language.sendRawMessage("Balance: &eFaction economy disabled", player);
        } else {
            Language.sendRawMessage("Balance: &e" + MainIF.getEconomy().format(faction.getFactionBank().balance().balance), player);
        }
        if (faction.isFrozen()) Language.sendRawMessage("&bFrozen", player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return FactionUtility.getAllFactions();
    }
}
