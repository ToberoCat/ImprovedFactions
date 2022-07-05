package io.github.toberocat.improvedfactions.reports;

import java.util.UUID;

public class Report {
    private UUID player;
    private String reason;
    private String faction;

    public Report() {}
    public Report(UUID player, String reason, String faction) {
        this.player = player;
        this.reason = reason;
        this.faction = faction;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }
}
