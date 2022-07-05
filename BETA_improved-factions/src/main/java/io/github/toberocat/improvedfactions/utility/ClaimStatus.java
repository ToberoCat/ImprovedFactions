package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.factions.Faction;

public class ClaimStatus {

    public enum Status { SUCCESS, ALREADY_CLAIMED, NEED_CONNECTION, NOT_CLAIMED, NOT_PROPERTY, NOT_ALLOWED_WORLD }

    private Status claimStatus;
    private Faction factionClaim;

    public ClaimStatus(Status claimStatus, Faction factionClaim) {
        this.claimStatus = claimStatus;
        this.factionClaim = factionClaim;
    }

    public Status getClaimStatus() {
        return claimStatus;
    }

    public Faction getFactionClaim() {
        return factionClaim;
    }
}
