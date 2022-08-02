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

    @Override
    public void migrate() {
        //migrateFactions();
        migrateChunks();
    }
 /*
    public static void migrateFactions() {
        DataManager factions = new DataManager(MainIF.getIF(), "Data/factions.yml");

        if (factions.getConfig().getConfigurationSection("f") == null) return;
        for (String key : factions.getConfig().getConfigurationSection("f").getKeys(false)) {
            String displayName = ChatColor.translateAlternateColorCodes('&', factions.getConfig().getString("f." + key + ".displayName"));

            String owner = factions.getConfig().getString("f." + key + ".owner");
            if (owner == null) {
                MainIF.logMessage(Level.WARNING, "Couldn't read owner from " + key + ". Faction couldn't get loaded, due too this error. Please fix it int he original file to be able to load it into this version");
                continue;
            }

            UUID ownerUUID = UUID.fromString(owner);

            LocalFaction faction = new LocalFaction(displayName, key, ownerUUID, LocalFaction.OpenType.INVITE_ONLY);

            // Adding players
            List<String> raw = factions.getConfig().getStringList("f." + key + ".members");
            for (String rawMember : raw) {
                Map.Entry<UUID, String> member = getFromFactionMember(rawMember);
                if (Bukkit.getOfflinePlayer(member.getKey()).isOnline()) {
                    faction.getFactionMemberManager().join(Bukkit.getPlayer(member.getKey()));
                } else {
                    // ToDo: Add a way to set persistent data for offline players
                    faction.getFactionMemberManager().getMembers().add(member.getKey());
                    faction.getFactionPerm().getMemberRanks().put(member.getKey(), member.getValue().replace("}", ""));
                    faction.getPowerManager()
                            .increaseMax(MainIF.config().getInt("power.maxPowerPerPlayer"));
                }
            }


            List<String> rawBanned = factions.getConfig().getStringList("f." + key + ".banned");
            ArrayList<UUID> banned = new ArrayList<>();

            for (String rawBan : rawBanned) {
                try {
                    rawBan = rawBan.replace("]", "").replace("[", "");
                    banned.add(UUID.fromString(rawBan.trim()));
                } catch (IllegalArgumentException exception) {
                    MainIF.logMessage(Level.WARNING, "&cCouldn't load banned for " + key + ". ");
                }
            }

            faction.getRelationManager().getAllies().addAll(factions.getConfig().getStringList("f." + key + ".allies"));
            faction.getRelationManager().getEnemies().addAll(factions.getConfig().getStringList("f." + key + ".enemies"));

            if (factions.getConfig().contains("f." + key + ".permanent"))
                faction.setPermanent(factions.getConfig().getBoolean("f." + key + ".permanent"));
            if (factions.getConfig().contains("f." + key + ".frozen"))
                faction.setFrozen(factions.getConfig().getBoolean("f." + key + ".frozen"));


            faction.getPowerManager().setCurrentPower(factions.getConfig().getInt("f." + key + ".power"));
            faction.setClaimedChunks(factions.getConfig().getInt("f." + key + ".claimedChunks"));
            faction.getFactionMemberManager().setBanned(banned);
            faction.setMotd(factions.getConfig().getString("f." + key + ".motd"));
            faction.setDescription(Collections.singletonList(factions.getConfig().getString("f." + key + ".description")).toArray(String[]::new));

            faction.setRegistryName(key);
            faction.setDisplayName(displayName);

            AsyncTask.runLaterSync(0, () -> {
                Bukkit.getPluginManager().callEvent(new FactionLoadEvent(faction));
                FileAccess.write("Factions", key, faction);
                LocalFaction.getLoadedFactions().put(key, faction);
            });
        }

        MainIF.logMessage(Level.INFO, "You can now safely delete the file factions.yml from the Data folder, if no warnings popped up before");
    }

    private static Map.Entry<UUID, String> getFromFactionMember(String str) {
        UUID uuid = null;
        String rank = null;
        String[] parms = str.split("[,=]");
        for (int i = 0; i < parms.length; i++) {
            String parm = parms[i];
            if (parm.contains("uuid")) {
                uuid = UUID.fromString(parms[i + 1]);
            }

            if (parm.contains("rank")) {
                rank = parms[i + 1];
            }
        }

        UUID finalUuid = uuid;
        String finalRank = rank;

        assert finalUuid != null;
        assert finalRank != null;

        return Map.entry(finalUuid, finalRank);
    }
*/
}
