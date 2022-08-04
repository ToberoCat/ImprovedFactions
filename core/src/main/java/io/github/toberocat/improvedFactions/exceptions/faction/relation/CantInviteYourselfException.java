package io.github.toberocat.improvedFactions.exceptions.faction.relation;

public class CantInviteYourselfException extends Exception {
    public CantInviteYourselfException() {
        super("You can't invite yourself");
    }
}
