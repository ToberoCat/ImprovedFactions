package io.github.toberocat.core.factions.claim;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.Claim;
import io.github.toberocat.core.utility.claim.ClaimManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class FactionClaims {
    private final LinkedHashMap<String, Stream<FactionClaim>> claims;

    public FactionClaims(@NotNull LinkedHashMap<String, Stream<FactionClaim>> claims) {
        this.claims = claims;
    }

    public static FactionClaims createClaims(@NotNull Faction faction) {
        LinkedHashMap<String, Stream<FactionClaim>> worldClaims = new LinkedHashMap<>();
        ClaimManager.CLAIMS.forEach((name, claims) -> {
            World world = Bukkit.getWorld(name);
            if (world == null || Utility.isDisabled(world)) return;

            worldClaims.put(name, claims.stream()
                    .filter(x -> x != null && x.getRegistry().equals(faction.getRegistry()))
                    .map(FactionClaim::fromClaim));
        });

        return new FactionClaims(worldClaims);
    }


    record FactionClaim(int x, int z) {

        public static FactionClaim fromClaim(@NotNull Claim claim) {
            return new FactionClaim(claim.getX(), claim.getY());
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FactionClaim that = (FactionClaim) o;
            return x == that.x && z == that.z;
        }
    }
}
