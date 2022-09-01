package io.github.toberocat.improvedFactions.core.listener;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
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

        int fromX = from.getX(), fromZ = from.getZ();
        int toX = to.getX(), toZ = to.getZ();
        if (fromX == toX && fromZ == toZ) return;

        WorldClaim claim = ClaimHandler.getWorldClaim(fromWorld);

        String fromRegistry = claim.getRegistry(fromX, fromZ);
        if (fromRegistry == null) fromRegistry = ClaimHandler.WILDERNESS_REGISTRY;

        String toRegistry = claim.getRegistry(toX, toZ);
        if (toRegistry == null) toRegistry = ClaimHandler.WILDERNESS_REGISTRY;

        if (fromRegistry.equals(toRegistry)) return;

        try {
            Faction<?> faction = FactionHandler.getFaction(toRegistry);
            player.sendTitle(faction.getDisplay(), "");
        } catch (FactionNotInStorage e) {
            String finalToRegistry = toRegistry;
            player.sendTitle(translatable -> translatable
                    .getZones()
                    .get(finalToRegistry.replaceAll(":", "")));

        }

    }
}
