package io.github.toberocat.improvedFactions.translator.layout.messages.faction;

import java.util.List;

public class FactionMessage {
    private List<String> player;
    private List<String> broadcast;

    public FactionMessage() {

    }

    public List<String> getPlayer() {
        return player;
    }

    public void setPlayer(List<String> player) {
        this.player = player;
    }

    public List<String> getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(List<String> broadcast) {
        this.broadcast = broadcast;
    }
}
