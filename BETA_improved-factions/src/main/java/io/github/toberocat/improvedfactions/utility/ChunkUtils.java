package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.event.chunk.ChunkClaimEvent;
import io.github.toberocat.improvedfactions.event.chunk.ChunkUnclaimEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ChunkUtils {

    public static List<Vector2> claimedChunks = new ArrayList<>();

    public static final NamespacedKey FACTION_CLAIMED_KEY = new NamespacedKey(ImprovedFactionsMain.getPlugin(), "faction-claimed");

    public static Faction GetFactionClaimedChunk(Chunk chunk) {
        if (chunk == null) return null;
        PersistentDataContainer container = chunk.getPersistentDataContainer();
        if (container.has(FACTION_CLAIMED_KEY, PersistentDataType.STRING)) {
            String factionRegistry = container.get(FACTION_CLAIMED_KEY, PersistentDataType.STRING);
            return FactionUtils.getFactionByRegistry(factionRegistry);
        }
        return null;
    }

    public static void UnClaimChunk(@NotNull Chunk chunk, @NotNull Faction faction, TCallback<ClaimStatus> callback) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();

        /*
        if (!ImprovedFactionsMain.getPlugin().getConfig().getStringList("general.worlds").contains(chunk.getWorld().getName())) {
            callback.Callback(new ClaimStatus(ClaimStatus.Status.NOT_ALLOWED_WORLD, null));
            return;
        }
         */

        if (!container.has(FACTION_CLAIMED_KEY, PersistentDataType.STRING)) {
            callback.Callback(new ClaimStatus(ClaimStatus.Status.NOT_CLAIMED, null));
            return;
        }

        Faction faction1 = GetFactionClaimedChunk(chunk);
            if (faction1 != faction) {
                callback.Callback(new ClaimStatus(ClaimStatus.Status.NOT_PROPERTY, null));
            } else if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.connectedChunks")) {
                List<Vector2> openList = new ArrayList<>();
                List<Vector2> closedList = new ArrayList<>();

                for (Chunk ng : GetNeighbourChunks(chunk)) {
                    Faction factionChunk = GetFactionClaimedChunk(ng);
                        if (factionChunk == faction) {
                            openList.add(new Vector2(ng.getX(), ng.getZ()));
                        }
                }

                while (!openList.isEmpty()) {
                    Vector2[] neighbours = GetNeighbourChunks(openList.get(0));
                    closedList.add(openList.get(0));
                    openList.remove(0);
                    for (Vector2 neighbour : neighbours) {
                        boolean cont = false;
                        for (Vector2 vec : closedList) {
                            if (neighbour.getX() == vec.getX() && neighbour.getY() == vec.getY()) {
                                cont = true;
                                break;
                            }
                        }
                        if (cont) continue;
                        if (openList.contains(neighbour))
                            continue;
                        Faction claimFaction = GetFactionClaimedChunk(chunk.getWorld().getChunkAt((int)neighbour.getX(), (int)neighbour.getY()));
                            if (claimFaction == faction && chunk != chunk.getWorld().getChunkAt((int)neighbour.getX(), (int)neighbour.getY())) { //The chunk is not wildness and isn't a part of another faction
                                openList.add(neighbour);
                            }
                    }
                }

                if (faction.getClaimedChunks() != closedList.size()+1) {
                    callback.Callback(new ClaimStatus(ClaimStatus.Status.NEED_CONNECTION, faction));
                }
            }

        RemoveChunk(chunk);
        container.remove(FACTION_CLAIMED_KEY);
        Bukkit.getPluginManager().callEvent(new ChunkUnclaimEvent(chunk, faction));
        callback.Callback(new ClaimStatus(ClaimStatus.Status.SUCCESS, null));
    }

    public static void ClaimChunk(@NotNull Chunk chunk, @NotNull Faction faction, TCallback<ClaimStatus> callback) {
        PersistentDataContainer container = chunk.getPersistentDataContainer();

        /*
        if (!ImprovedFactionsMain.getPlugin().getConfig().getStringList("general.worlds").contains(chunk.getWorld().getName())) {
            callback.Callback(new ClaimStatus(ClaimStatus.Status.NOT_ALLOWED_WORLD, null));
            return;
        }
         */

        //<editor-fold desc="ConnectedChunks check">
        if (ImprovedFactionsMain.getPlugin().getConfig().getBoolean("general.connectedChunks")
                && faction.getClaimedChunks() != 0) {
            AtomicBoolean connected = new AtomicBoolean(false);
            for (Chunk neighbour : GetNeighbourChunks(chunk)) {
                Faction chunkClaimedFaction = GetFactionClaimedChunk(neighbour);
                    if (chunkClaimedFaction != null && chunkClaimedFaction == faction) {
                        connected.set(true);
                    }
                if (connected.get()) break;
            }
            if (!connected.get()) {
                Faction faction1 = GetFactionClaimedChunk(chunk);
                    callback.Callback(new ClaimStatus(ClaimStatus.Status.NEED_CONNECTION, faction1));
            }
        }
        //</editor-fold>

        if (!canOverclaim(chunk, faction)) {
            Faction faction1 = GetFactionClaimedChunk(chunk);
            callback.Callback(new ClaimStatus(ClaimStatus.Status.ALREADY_CLAIMED, faction1));
            return;
        }

        Faction cc = GetFactionClaimedChunk(chunk);
        if (cc != null) cc.getPowerManager().unclaimChunk();

        container.set(FACTION_CLAIMED_KEY, PersistentDataType.STRING, faction.getRegistryName());
        AddChunk(chunk);
        Bukkit.getPluginManager().callEvent(new ChunkClaimEvent(chunk, faction));
        Faction faction1 = GetFactionClaimedChunk(chunk);
            callback.Callback(new ClaimStatus(ClaimStatus.Status.SUCCESS, faction1));
    }

    private static boolean canOverclaim(Chunk chunk, Faction wantToClaimFaction) {
        Faction faction = GetFactionClaimedChunk(chunk);

        if (faction == null) return true;
        if (faction.getRegistryName().equals(wantToClaimFaction.getRegistryName())) return false;

        if (faction.getPowerManager().getPower() >= faction.getClaimedChunks()) return false;

        if (!isCorner(chunk, wantToClaimFaction.getRegistryName())) return false;
        for (Player player : FactionUtils.getPlayersOnline(faction)) {
            player.sendMessage(Language.getPrefix() +
                    Language.format("&6&lWarning: &e" +
                            wantToClaimFaction.getDisplayName() + "&f claimed a chunk from your land!"));
        }
        return true;
    }

    private static boolean isCorner(Chunk chunk, String rg) {
        Chunk[] neighbours = GetNeighbourChunks(chunk);
        for (Chunk neighbour : neighbours) {
            if (GetFactionClaimedChunk(neighbour) == null ||
                    GetFactionClaimedChunk(chunk).getRegistryName().equals(rg)) return true;
        }
        return false;
    }

    public static Vector2[] GetNeighbourChunks(Vector2 chunk) {
        Vector2[] neighbours = new Vector2[4];

        neighbours[0] = new Vector2(chunk.getX() - 1, chunk.getY());
        neighbours[2] = new Vector2(chunk.getX() + 1, chunk.getY());

        neighbours[1] = new Vector2(chunk.getX(), chunk.getY() - 1);
        neighbours[3] = new Vector2(chunk.getX(), chunk.getY() + 1);

        return neighbours;
    }
    public static Chunk[] GetNeighbourChunks(Chunk chunk) {
        Chunk[] neighbours = new Chunk[4];
        int centerX = chunk.getX();
        int centerZ = chunk.getZ();

        neighbours[0] = chunk.getWorld().getChunkAt(centerX - 1, centerZ);
        neighbours[2] = chunk.getWorld().getChunkAt(centerX + 1, centerZ);

        neighbours[1] = chunk.getWorld().getChunkAt(centerX, centerZ - 1);
        neighbours[3] = chunk.getWorld().getChunkAt(centerX, centerZ + 1);

        return neighbours;
    }

    public static boolean ContainsChunk(Chunk chunk) {
        int x = chunk.getX();
        int y = chunk.getZ();
        for (Vector2 vect : claimedChunks) {
            if (vect.getX() == x && vect.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private static void RemoveChunk(Chunk chunk) {
        if (ContainsChunk(chunk)) {
            claimedChunks.remove(new Vector2(chunk.getX(), chunk.getZ()));
        }
    }

    private static void AddChunk(Chunk chunk) {
        if (!ContainsChunk(chunk)) {
            claimedChunks.add(new Vector2(chunk.getX(), chunk.getZ()));
        }
    }

    public static void Save() {
        ImprovedFactionsMain.getPlugin().getChunkData().getConfig().set("claimedChunks", Utils.listToStringList(claimedChunks));
        ImprovedFactionsMain.getPlugin().getChunkData().saveConfig();
    }

    public static void Init() {
        claimedChunks = new ArrayList<>();

        for (String raw : ImprovedFactionsMain.getPlugin().getChunkData().getConfig().getStringList("claimedChunks")) {
            claimedChunks.add(Vector2.FromString(raw));
        }

    }
}
