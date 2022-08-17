package io.github.toberocat.improvedFactions.core.claims.worldclaim.local;

import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.handler.WorldClaimHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedHashMap;

public class LocalWorldClaimHandler extends WorldClaimHandler {

    private static final LinkedHashMap<String, FileAccess> ACCESS_MAP = new LinkedHashMap<>();

    @Override
    public @NotNull WorldClaim createWorldClaim(@NotNull World<?> world) {

        return new LocalWorldClaim(ACCESS_MAP
                .computeIfAbsent(world.getWorldName(), (k) -> new FileAccess(
                        new File(ImprovedFactions.api().getLocalFolder(),
                                FileAccess.CHUNKS_FOLDER + "/" + k))));
    }
}
