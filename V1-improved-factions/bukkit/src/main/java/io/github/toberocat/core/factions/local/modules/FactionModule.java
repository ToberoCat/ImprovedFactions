package io.github.toberocat.core.factions.local.modules;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.toberocat.core.factions.local.LocalFaction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class FactionModule {
    public static final String MESSAGE_MODULE_ID = "message_module";
    @JsonIgnore
    protected LocalFaction faction;

    public FactionModule(LocalFaction faction) {
        this.faction = faction;
    }

    @JsonIgnore
    public LocalFaction getFaction() {
        return faction;
    }

    @JsonIgnore
    public FactionModule setFaction(LocalFaction faction) {
        this.faction = faction;
        return this;
    }
}
