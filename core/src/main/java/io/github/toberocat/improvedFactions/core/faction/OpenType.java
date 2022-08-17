package io.github.toberocat.improvedFactions.core.faction;

public enum OpenType {
    PUBLIC("Public"), INVITE_ONLY("Invite only"), CLOSED("Closed");

    final String display;

    OpenType(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
