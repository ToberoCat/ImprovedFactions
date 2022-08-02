package io.github.toberocat.core.utility.exceptions.faction.relation;

import org.jetbrains.annotations.NotNull;

public class CantInviteYourselfException extends Exception {
    public CantInviteYourselfException() {
        super("You can't invite yourself");
    }
}
