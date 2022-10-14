package io.github.toberocat.improvedFactions.core.listener;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class MoveListener {

    public void move(@NotNull FactionPlayer<?> player, @NotNull Chunk<?> from, @NotNull Chunk<?> to) {
        World<?> fromWorld = from.getWorld();
        World<?> toWorld = to.getWorld();
        if (!fromWorld.getWorldName().equals(toWorld.getWorldName())) return;

        int fromX = from.getX();
        int fromZ = from.getZ();
        int toX = to.getX();
        int toZ = to.getZ();
        if (fromX == toX && fromZ == toZ) return;

        WorldClaim fromClaim = ClaimHandler.getWorldClaim(fromWorld);
        String fromRegistry = fromClaim.getRegistry(fromX, fromZ);
        if (fromRegistry == null) fromRegistry = ClaimHandler.WILDERNESS_REGISTRY;

        WorldClaim toClaim = ClaimHandler.getWorldClaim(toWorld);
        String toRegistry = toClaim.getRegistry(toX, toZ);
        if (toRegistry == null) toRegistry = ClaimHandler.WILDERNESS_REGISTRY;

        if (fromRegistry.equals(toRegistry)) return;

        try {
            Faction<?> faction = FactionHandler.getFaction(toRegistry);
            player.sendTitle(faction.getDisplay(), "");
        } catch (FactionNotInStorage e) {
            String translationId;
            Zone zone = ClaimHandler.getZone(toRegistry);
            if (zone != null) {
                if (!zone.managed()) return;
                translationId = zone.translationId();
            } else {
                translationId = toRegistry.replaceAll(":", "");
            }

            player.sendTitle(translatable -> translatable
                    .getZones()
                    .get(translationId));

        }

    }
}
