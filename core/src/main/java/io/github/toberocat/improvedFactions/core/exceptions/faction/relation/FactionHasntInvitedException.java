package io.github.toberocat.improvedFactions.core.exceptions.faction.relation;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.jetbrains.annotations.NotNull;

public class FactionHasntInvitedException extends FactionException {
    public FactionHasntInvitedException(@NotNull Faction<?> inviter, @NotNull Faction<?> invited) {
        super(invited, "exceptions.faction-hasnt-invited", new PlaceholderBuilder()
                .placeholder("inviter", inviter)
                .placeholder("invited", invited)
                .getPlaceholders());
    }
}
