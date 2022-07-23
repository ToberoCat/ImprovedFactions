package io.github.toberocat.core.utility.exceptions.chunks;

public class ChunkAlreadyClaimedException extends Exception {
    public ChunkAlreadyClaimedException() {
        super("The chunk got already claimed");
    }
}
