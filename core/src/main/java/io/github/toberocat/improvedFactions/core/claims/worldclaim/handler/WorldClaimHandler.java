package io.github.toberocat.improvedFactions.core.claims.worldclaim.handler;

import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.database.mysql.MySqlWorldClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.local.LocalWorldClaimHandler;
import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class WorldClaimHandler {
    private static final WorldClaimHandler implementation = createImplementation();

    private static WorldClaimHandler createImplementation() {
        if (ImprovedFactions.api().getConfig().getBool("storage.use-mysql", false))
            return new MySqlWorldClaimHandler();
        return new LocalWorldClaimHandler();
    }

    public static WorldClaimHandler api() {
        return implementation;
    }

    public abstract @NotNull WorldClaim createWorldClaim(@NotNull World world);
}
