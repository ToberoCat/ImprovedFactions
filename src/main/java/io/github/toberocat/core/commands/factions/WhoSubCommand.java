package io.github.toberocat.core.commands.factions;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class WhoSubCommand extends SubCommand {
    public WhoSubCommand() {
        super("who", "", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        String factionRegistry;
        if (args.length == 0) {
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


        /*
        ============================
        ===== Faction Name =========
        ============================
        Description: Love Faction
        Members online: 3/20
        Power Faction: 75/200
        Chunk Claim: 90/100
        Wars: Warlords,ZoneTwon,ZoneThree
        Ally: KillWarlords,ZoneFour
        Balance: 98.103.200
        Upgrades buy: 0/10
        Freeze (Only spawn when factions are frozen)
         */

    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
