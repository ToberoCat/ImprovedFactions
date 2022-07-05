package io.github.toberocat.improvedfactions.types;

public enum ChatType {
    PUBLIC("&bpublic"), FACTION("&cfaction"), ALLIES("&dallies");

    private final String display;
    ChatType(String display) {
        this.display = display;
    }
    public String getDisplay() {
        return display;
    }
}
