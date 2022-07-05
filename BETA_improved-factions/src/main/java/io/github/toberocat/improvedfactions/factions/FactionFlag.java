package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.gui.Flag;

public class FactionFlag {
    private String id;
    private Flag flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Flag getFlag() {
        return flag;
    }

    public void setFlag(Flag flag) {
        this.flag = flag;
    }

    public FactionFlag(String id, Flag flag) {
        this.id = id;
        this.flag = flag;
    }
}
