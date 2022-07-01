package io.github.toberocat.core.factions.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.toberocat.core.factions.Faction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class FactionModule {
    public static final String MESSAGE_MODULE_ID = "message_module";
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
