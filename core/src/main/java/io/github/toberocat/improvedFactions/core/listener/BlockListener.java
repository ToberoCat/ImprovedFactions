package io.github.toberocat.improvedFactions.core.listener;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.permission.FactionPermission;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockListener {
    public boolean breakBlock(@NotNull FactionPlayer<?> player,
                              @NotNull World<?> world,
                              int chunkX, int chunkZ)
            throws FactionNotInStorage {
        String registry = ClaimHandler.getWorldClaim(world).getRegistry(chunkX, chunkZ);
        if (registry == null) return false;

        String pRegistry = player.getFactionRegistry();
        if (pRegistry == null) return true;
        if (pRegistry.equals(registry)) return false;

        Faction<?> claimFaction = FactionHandler.getFaction(registry);
        return !claimFaction.hasPermission(FactionPermission.BREAK_PERMISSION,
                claimFaction.getPlayerRank(player));
    }
}
