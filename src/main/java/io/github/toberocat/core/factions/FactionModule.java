package io.github.toberocat.core.factions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class FactionModule {
    @JsonIgnore
    protected Faction faction;

    public FactionModule(Faction faction) {
        this.faction = faction;
    }

    @JsonIgnore
    public Faction getFaction() {
        return faction;
    }

    @JsonIgnore
    public FactionModule setFaction(Faction faction) {
        this.faction = faction;
        return this;
    }
}
