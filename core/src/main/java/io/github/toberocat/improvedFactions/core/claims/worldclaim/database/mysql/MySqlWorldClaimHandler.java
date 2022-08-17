package io.github.toberocat.improvedFactions.core.claims.worldclaim.database.mysql;

import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.handler.WorldClaimHandler;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public class MySqlWorldClaimHandler extends WorldClaimHandler {
    @Override
    public @NotNull WorldClaim createWorldClaim(@NotNull World<?> world) {
        return new MySqlWorldClaim(world.getWorldName());
    }
}
