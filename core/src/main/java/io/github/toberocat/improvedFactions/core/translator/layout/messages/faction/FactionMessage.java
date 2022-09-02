package io.github.toberocat.improvedFactions.core.translator.layout.messages.faction;

import java.util.Map;

public class FactionMessage {
    private Map<String, String> player;
    private Map<String, String> broadcast;

    public FactionMessage() {

    }

    public Map<String, String> getPlayer() {
        return player;
    }

    public void setPlayer(Map<String, String> player) {
        this.player = player;
    }

    public Map<String, String> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(Map<String, String> broadcast) {
        this.broadcast = broadcast;
    }
}
