package io.github.toberocat.improvedfactions.particle;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import io.github.toberocat.improvedfactions.CacheCoreExtension;
import io.github.toberocat.improvedfactions.ClaimParticleExtension;
import io.github.toberocat.improvedfactions.data.PositionPair;
import io.github.toberocat.improvedfactions.data.Shape;
import io.github.toberocat.improvedfactions.mesh.WorldCache;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.*;
import java.util.stream.Stream;

import static io.github.toberocat.core.utility.Utility.clamp;

public class ParticleHandler {
    private final HashMap<UUID, ParticlePerformance> displayParticles;
    private final int taskId;

    public ParticleHandler(MainIF plugin) {
        displayParticles = new HashMap<>();

        taskId = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (MainIF.getIF() == null) return;
            for (Map.Entry<UUID, ParticlePerformance> entry : new HashMap<>(displayParticles).entrySet()) {
                ParticlePerformance performance = entry.getValue();
                if (performance == ParticlePerformance.HIDDEN) return;

                Player player = Bukkit.getPlayer(entry.getKey());
                if (player == null || !player.isOnline()) continue;
                sendParticles(player, performance);
            }
        }, 5, ClaimParticleExtension.VISUALISATION_INTERVAL).getTaskId();
    }

    public void dispose() {
        displayParticles.clear();
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void update(Player player, int selected) {
        displayParticles.replace(player.getUniqueId(), ParticlePerformance.values()[selected]);
    }

    public void addPlayer(Player player) {
        displayParticles.put(player.getUniqueId(), getPerformance(player.getUniqueId()));
    }

    public void removePlayer(Player player) {
        displayParticles.remove(player.getUniqueId());
    }

    private void createParticle(Player player, double x, double y, double z, Particle.DustOptions dust) {
        Particle particle = ClaimParticleExtension.PARTICLE;
        if (particle == Particle.REDSTONE) player.spawnParticle(
                ClaimParticleExtension.PARTICLE,
                new Location(player.getWorld(), x, y, z),
                0,
                dust
        );
        else player.spawnParticle(
                ClaimParticleExtension.PARTICLE,
                new Location(player.getWorld(), x, y, z),
                0
        );
    }

    private ParticlePerformance getPerformance(UUID uuid) {
        Setting<?> setting = PlayerSettings.getSettings(uuid).getPlayerSetting()
                .get(ClaimParticleExtension.PERFORMANCE_SETTING);
        if (setting instanceof EnumSetting e)
            return ParticlePerformance.values()[e.getSelected()];
        return ParticlePerformance.HIDDEN;
    }

    private void sendParticles(Player player, ParticlePerformance performance) {
        BoundingBox playerViewBox = getPlayerViewBox(clamp(player.getClientViewDistance(),1,
                ClaimParticleExtension.MAX_PARTICLE_RENDER_DISTANCE), player);

        WorldCache worldCache = CacheCoreExtension.meshCache.getCache(player.getWorld().getName());
        Stream<Map.Entry<String, Stream<Shape>>> renderShapes = worldCache.getShapes(playerViewBox);

        renderShapes.forEach((entry) -> {
            String registry = entry.getKey();
            entry.getValue().forEach((shape -> shape.lines().forEach(line -> {
                for (int i = 0; i < line.getLocations().size(); i += performance.density) {
                    PositionPair pair = line.getLocations().get(i);

                    Color c = Math.random() < 0.2 ? Color.BLACK : FactionUtility.getRegistryColor(registry);
                    if (Math.random() < 0.2) c = Color.WHITE;

                    Location loc = player.getLocation();

                    for (Integer height : pair.cache().heights()) {
                        float size = (float) clamp(loc.toVector().distanceSquared(pair.toVector(height)) /
                                20, 2, 10);
                        if (size >= ClaimParticleExtension.PARTICLE_HIDE_DST)
                            continue;

                        Particle.DustOptions dust = new Particle.DustOptions(c, size);
                        for (int y = 0; y < ClaimParticleExtension.PARTICLE_HEIGHT; y++) {
                            createParticle(player, pair.x() + 0.5, height + y + 0.5,
                                    pair.z() + 0.5, dust);
                        }
                    }
                }
            })));
        });
    }

    private BoundingBox getPlayerViewBox(int renderDst, Player player) {
        Chunk chunk = player.getLocation().getChunk();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        World world = player.getWorld();
        return new BoundingBox(
                chunkX - renderDst, world.getMinHeight(), chunkZ - renderDst,
                chunkX + renderDst, world.getMaxHeight(), chunkZ + renderDst
        );
    }

}
