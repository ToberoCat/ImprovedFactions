package io.github.toberocat.improvedFactions.core.listener;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockListener {
    public boolean breakBlock(@NotNull FactionPlayer<?> player,
                              @NotNull World<?> world,
                              int chunkX, int chunkZ) {
        String registry = ClaimHandler.getWorldClaim(world).getRegistry(chunkX, chunkZ);
        if (registry == null) return false;

        String pRegistry = player.getFactionRegistry();
        if (pRegistry)
    }
}
