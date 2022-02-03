package io.github.toberocat.core.utility.history.territory;

public class TerritorySwitch {
    private Territory from;
    private Territory to;

    public TerritorySwitch() {}

    public TerritorySwitch(Territory from, Territory to) {
        this.from = from;
        this.to = to;
    }

    public Territory getFrom() {
        return from;
    }

    public void setFrom(Territory from) {
        this.from = from;
    }

    public Territory getTo() {
        return to;
    }

    public void setTo(Territory to) {
        this.to = to;
    }
}
