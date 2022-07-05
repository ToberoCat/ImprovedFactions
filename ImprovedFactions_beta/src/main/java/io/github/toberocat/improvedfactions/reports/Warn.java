package io.github.toberocat.improvedfactions.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedfactions.factions.Faction;

import java.util.ArrayList;
import java.util.HashMap;

public class Warn {
    public HashMap<String, Integer> WARNS = new HashMap<>();

    public Warn() {}

    @JsonIgnore
    public int addWarn(Faction faction) {
        if (WARNS.containsKey(faction.getRegistryName())) {
            int value = WARNS.get(faction.getRegistryName());

            if (value + 1 >= 5) {
                faction.DeleteFaction();
            }

            WARNS.replace(faction.getRegistryName(), value + 1);
            return value+1;
        } else {
            WARNS.put(faction.getRegistryName(), 1);
            return 1;
        }

    }

    public HashMap<String, Integer> getWARNS() {
        return WARNS;
    }

    public void setWARNS(HashMap<String, Integer> WARNS) {
        this.WARNS = WARNS;
    }
}
