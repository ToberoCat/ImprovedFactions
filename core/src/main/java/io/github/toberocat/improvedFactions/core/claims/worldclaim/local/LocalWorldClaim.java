package io.github.toberocat.improvedFactions.core.claims.worldclaim.local;

import io.github.toberocat.improvedFactions.core.claims.component.Claim;
import io.github.toberocat.improvedFactions.core.claims.worldclaim.WorldClaim;
import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class LocalWorldClaim implements WorldClaim {

    private static final long CLEAR_INTERVAL = ConfigHandler.api()
            .getLong("performance.ram.claim-garbage-collector", 6000L);

    private final FileAccess access;
    private final String worldName;
    private final int taskId;

    private final HashMap<XZPair, String> cachedClaims;

    public LocalWorldClaim(@NotNull String worldName, @NotNull FileAccess access) {
        this.access = access;
        this.worldName = worldName;
        this.cachedClaims = new HashMap<>();

        taskId = ImprovedFactions.api()
                .getScheduler()
                .runTimer(cachedClaims::clear, CLEAR_INTERVAL);
    }

    @Override
    public void add(@NotNull String registry, int x, int z) {
        try {
            access.write(new Claim(worldName, x, z, claim -> registry),
                    String.format("%d_%d", x, z));
            cachedClaims.put(new XZPair(x, z), registry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int x, int z) {
        access.delete(String.format("%d_%d", x, z));
        cachedClaims.remove(new XZPair(x, z));
    }

    @Override
    public @NotNull Stream<Claim> getAllClaims() {
        return Arrays.stream(access.list())
                .map(n -> {
                    String[] split = n.split("_");
                    int x = Integer.parseInt(split[0]);
                    int z = Integer.parseInt(split[1]);

                    return new Claim(worldName, x, z, claim -> {
                        try {
                            return access.read(String.class, n);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "";
                        }
                    });
                });
    }

    @Override
    public @NotNull String getRegistry(int x, int z) {
        XZPair pair = new XZPair(x, z);
        if (cachedClaims.containsKey(pair)) return cachedClaims.get(pair);

        try {
            String registry = access.read(String.class, String.format("%d_%d", x, z));
            cachedClaims.put(pair, registry);

            return registry;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void dispose() {
        ImprovedFactions.api().getScheduler().cancel(taskId);
        cachedClaims.clear();
    }
}
