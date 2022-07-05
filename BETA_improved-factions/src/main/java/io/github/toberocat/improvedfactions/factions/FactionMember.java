package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.ranks.Rank;

import java.util.Arrays;
import java.util.UUID;

public class FactionMember {

    private UUID uuid;
    private Rank rank;

    public FactionMember(UUID uuid, Rank rank) {
        this.uuid = uuid;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "FactionMember{" +
                "uuid=" + uuid.toString() +
                ", rank=" + rank.toString() +
                '}';
    }

    public static FactionMember fromString(String str) {
        UUID uuid = null;
        Rank rank = null;
        String[] parms = str.split("[,=]");
        for (int i = 0; i < parms.length; i++) {
            String parm = parms[i];
            if (parm.contains("uuid")) {
                uuid = UUID.fromString(parms[i + 1]);
            }
            if (parm.contains("rank")) {
                rank = Rank.fromString(parms[i + 1].replace("}", ""));
            }
        }
        if (uuid == null || rank == null) {
            return null;
        }
        return new FactionMember(uuid, rank);
    }
}
