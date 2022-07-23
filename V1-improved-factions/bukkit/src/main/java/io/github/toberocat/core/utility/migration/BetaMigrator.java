package io.github.toberocat.core.utility.migration;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

import static io.github.toberocat.core.utility.claim.ClaimManager.protectChunk;

public class BetaMigrator implements Migrator {
    @Override
    public void migrate() {
        migrateChunks();
    }

    public static void migrateChunks() {
        NamespacedKey persistentData = new NamespacedKey(MainIF.getIF(), "faction-claimed");

        DataManager chunks = new DataManager(MainIF.getIF(), "Data/chunkData.yml");
        for (String raw : chunks.getConfig().getStringList("claimedChunks")) {
            int x = Integer.parseInt(raw.split(" ")[0]);
            int z = Integer.parseInt(raw.split(" ")[1]);

            String registry = null;
            Chunk chunk = null;
            for (World world : Bukkit.getWorlds()) {
                chunk = world.getChunkAt(x, z);
                registry = chunk.getPersistentDataContainer().get(persistentData, PersistentDataType.STRING);
                if (registry != null) break;
            }

            if (registry == null) continue;

            if (registry.equals("safezone")) registry = ClaimManager.SAFEZONE_REGISTRY;

            String finalRegistry = registry;
            Chunk finalChunk = chunk;
            AsyncTask.run(() -> {
                try {
                    protectChunk(finalRegistry, finalChunk);
                } catch (ChunkAlreadyClaimedException e) {
                    e.printStackTrace();
                }
            });
        }

        MainIF.logMessage(Level.INFO, "Migrated every chunk. You can now delete the chunkData.yml file in Data folder safely without worrying, if no warnings / errors appear above");
    }

}
