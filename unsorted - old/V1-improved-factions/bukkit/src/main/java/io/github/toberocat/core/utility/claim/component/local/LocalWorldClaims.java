package io.github.toberocat.core.utility.claim.component.local;

import io.github.toberocat.core.utility.claim.component.Claim;
import io.github.toberocat.core.utility.claim.component.WorldClaims;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class LocalWorldClaims extends WorldClaims {

    public LocalWorldClaims(@NotNull String worldName) {
        super(worldName);
    }

    public static LocalWorldClaims loadWorldClaim(@NotNull String worldName) {

    }


    @Override
    public void add(@NotNull Claim claim) {

    }

    @Override
    public void remove(@NotNull Claim claim) {

    }

    @Override
    public void remove(int x, int z) {

    }

    @Override
    public Stream<Claim> getClaims() {
        return null;
    }

    @Override
    public void save() {

    }
}
