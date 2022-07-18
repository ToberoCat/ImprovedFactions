package io.github.toberocat.core.utility.exceptions.faction;

public class FactionHandlerNotFound extends Exception {
    public FactionHandlerNotFound() {
        super("FactionHandler wasn't to be found. This is a critical bug, please report it to the dev on discord or github");
    }
}
