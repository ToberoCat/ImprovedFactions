package io.github.toberocat.improvedfactions.command;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.Position;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static io.github.toberocat.improvedfactions.MassClaimExtension.POSITIONS;

public class MassClaimPos1 extends SubCommand {

    public MassClaimPos1() {
        super("pos1", "claim.pos1", "command.claim-mass.pos1.description", false);
    }

    public static void pos1(Player player, Location location) {
        UUID uuid = player.getUniqueId();

        if (POSITIONS.containsKey(uuid)) {
            POSITIONS.get(uuid).setPos1(location);
        } else {
            POSITIONS.put(uuid, new Position(location, null));
        }
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        pos1(player, new Location(player.getWorld(), player.getLocation().getX(), 0, player.getLocation().getZ()));
        Language.sendMessage("command.claim-mass.pos1.success", player);
    }

    @Override
    protected List<String> CommandTab(Player arg0, String[] arg1) {
        return null;
    }

}
