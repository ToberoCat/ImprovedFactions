package io.github.toberocat.improvedFactions.core.claims.worldclaim.local;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class LocalWorldClaim implements WorldClaim {

    private final FileAccess access;

    public LocalWorldClaim(FileAccess access) {
        this.access = access;
    }

    @Override
    public void add(@NotNull String registry, int x, int z) {

    }

    @Override
    public void remove(int x, int z) {

    }

    @Override
    public @NotNull Stream<Claim> getAllClaims() {
        return null;
    }

    @Override
    public @NotNull String getRegistry(int x, int z) {
        return null;
    }

    @Override
    public void dispose() { // Save world claim

    }
}
