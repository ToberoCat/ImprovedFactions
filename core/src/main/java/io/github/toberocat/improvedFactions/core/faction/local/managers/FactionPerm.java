package io.github.toberocat.improvedFactions.core.faction.local.managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.faction.Faction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FactionPerm {

    private Map<UUID, String> memberRanks;

    @JsonIgnore
    private Faction<?> faction;

    public FactionPerm() {
    }

    public FactionPerm(Faction<?> faction) {
        this.faction = faction;
        memberRanks = new HashMap<>();
    }

    public Map<UUID, String> getMemberRanks() {
        return memberRanks;
    }

    public void setMemberRanks(Map<UUID, String> memberRanks) {
        this.memberRanks = memberRanks;
    }

    @JsonIgnore
    public Faction<?> getFaction() {
        return faction;
    }

    @JsonIgnore
    public void setFaction(Faction<?> faction) {
        this.faction = faction;
    }
}
