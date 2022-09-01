package io.github.toberocat.improvedFactions.core.exceptions.faction;

public class FactionDoesntOwnChunkException extends Exception {
    private final String actualClaim;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public FactionDoesntOwnChunkException(String actualClaim) {
        this.actualClaim = actualClaim;
    }

    public String getActualClaim() {
        return actualClaim;
    }
}
