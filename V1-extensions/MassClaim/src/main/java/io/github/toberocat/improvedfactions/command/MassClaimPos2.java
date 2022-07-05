package io.github.toberocat.improvedfactions.command;


import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.Position;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static io.github.toberocat.improvedfactions.MassClaimExtension.POSITIONS;

public class MassClaimPos2 extends SubCommand {

    public MassClaimPos2() {
        super("pos2", "claim.mass.pos2", "command.claim-mass.pos2.description", false);
    }

    public static void pos2(Player player, Location location) {
        UUID uuid = player.getUniqueId();

        if (POSITIONS.containsKey(uuid)) {
            POSITIONS.get(uuid).setPos2(location);
        } else {
            POSITIONS.put(uuid, new Position(null, location));
        }
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        pos2(player, new Location(player.getWorld(), player.getLocation().getX(), 0, player.getLocation().getZ()));
        Language.sendMessage("command.claim-mass.pos2.success", player);
    }

    @Override
    protected List<String> CommandTab(Player arg0, String[] arg1) {
        return null;
    }

}
