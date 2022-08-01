package io.github.toberocat.improvedFactions.faction;

public enum OpenType {
    PUBLIC("Public"), INVITE_ONLY("Invite only"), CLOSED("Closed");

    String display;

    OpenType(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
