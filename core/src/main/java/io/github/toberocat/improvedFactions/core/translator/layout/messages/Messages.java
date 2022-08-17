package io.github.toberocat.improvedFactions.core.translator.layout.messages;

import io.github.toberocat.improvedFactions.core.translator.layout.messages.faction.FactionMessage;

public class Messages {
    private FactionMessage faction;

    public Messages() {
    }

    public FactionMessage getFaction() {
        return faction;
    }

    public void setFaction(FactionMessage faction) {
        this.faction = faction;
    }
}
