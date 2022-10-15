package io.github.toberocat.improvedFactions.core.listener;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class BlockListener {
    public boolean breakBlock(@NotNull FactionPlayer<?> player,
                              @NotNull World<?> world,
                              int chunkX, int chunkZ)
            throws FactionNotInStorage {
        String registry = ClaimHandler.getWorldClaim(world).getRegistry(chunkX, chunkZ);
        if (registry == null) return false;

        Zone zone = ClaimHandler.getZone(registry);
        if (zone != null) return zone.managed() && zone.protection();

        Faction<?> faction = FactionHandler.getFaction(registry);

        return !faction.hasPermission(FactionPermission.BREAK_PERMISSION, faction.getPlayerRank(player));
    }

    public boolean placeBlock(@NotNull FactionPlayer<?> player,
                              @NotNull World<?> world,
                              int chunkX, int chunkZ)
            throws FactionNotInStorage {
        String registry = ClaimHandler.getWorldClaim(world).getRegistry(chunkX, chunkZ);
        if (registry == null) return false;

        Zone zone = ClaimHandler.getZone(registry);
        if (zone != null) return zone.managed() && zone.protection();

        Faction<?> faction = FactionHandler.getFaction(registry);

        return !faction.hasPermission(FactionPermission.PLACE_PERMISSION, faction.getPlayerRank(player));
    }
}
