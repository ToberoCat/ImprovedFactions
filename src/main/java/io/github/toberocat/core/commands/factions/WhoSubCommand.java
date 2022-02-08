package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class WhoSubCommand extends SubCommand {
    public WhoSubCommand() {
        super("who", "", false);
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
        Language.sendRawMessage(topBottomMessage, player);
        Language.sendRawMessage("====  " + displayName + "  ====", player);
        Language.sendRawMessage(topBottomMessage, player);

        for (String description : faction.getDescription()) {
            Language.sendRawMessage("Description: " + description, player);
        }

        Language.sendRawMessage("Members online: " +
                faction.getFactionMemberManager().getOnlinePlayers().size() + "/" +
                faction.getFactionMemberManager().getMembers().size(), player);

        Language.sendRawMessage("Power: " +
                faction.getPowerManager().getCurrentPower() + "/" +
                faction.getPowerManager().getMaxPower(), player);

        Language.sendRawMessage("Chunk claim: " +
                faction.getClaimedChunks() + "/" +
                faction.getPowerManager().getCurrentPower(), player);

        Language.sendRawMessage("Wars: " +
                String.join(", ", faction.getRelationManager().getEnemies()), player);

        Language.sendRawMessage("Ally: " +
                String.join(", ", faction.getRelationManager().getAllies()), player);

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
