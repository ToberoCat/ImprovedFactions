package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsAlreadyInFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsBannedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.JoinWithRankInvalidException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerAlreadyMemberException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasBeenInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasntBeenInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.player.PlayerNotFoundException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InviteHandler {

    private static final PlayerHasBeenInvitedException HAS_BEEN_INVITED = new PlayerHasBeenInvitedException();

    public static void sendInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                  @NotNull FactionPlayer<?> sender,
                                  @NotNull Faction<?> faction,
                                  @NotNull FactionRank rank)
            throws PlayerHasBeenInvitedException, CantInviteYourselfException,
            PlayerIsAlreadyInFactionException, PlayerAlreadyMemberException {
        if (hasInvite(sender, faction)) throw HAS_BEEN_INVITED;
        if (sender.getUniqueId() == receiver.getUniqueId()) throw new CantInviteYourselfException();
        if (faction.isMember(receiver)) throw new PlayerAlreadyMemberException();
        if (receiver.inFaction()) throw new PlayerIsAlreadyInFactionException(faction, receiver);

        PersistentInvites.writeReceiver(receiver.getDataContainer(),
                sender.getUniqueId(),
                faction.getRegistry(),
                rank.getRegistry());

        PersistentInvites.writeSender(sender.getDataContainer(),
                receiver.getUniqueId());

        faction.broadcastTranslatable(translatable -> translatable
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .get("invite-sent"),
                new Placeholder("{sender}", sender.getName()),
                new Placeholder("{receiver}", receiver.getName()));

        EventExecutor.getExecutor().invitePlayer(receiver, sender, faction, rank);
    }

    public static void cancelInvite(@NotNull OfflineFactionPlayer<?> invited,
                                    @NotNull Faction<?> faction)
            throws PlayerHasntBeenInvitedException, PlayerNotFoundException {
        PersistentInvites.PersistentReceivedInvite invite = PersistentInvites.removeInvite(invited, faction.getRegistry());

        faction.broadcastTranslatable(translatable -> translatable
                .getMessages()
                .getFaction()
                .getBroadcast()
                .get("invite-cancelled"));

        EventExecutor.getExecutor().cancelInvite(invited,
                invite.sender(),
                faction, invite.rank());
    }

    public static void acceptInvite(@NotNull FactionPlayer<?> invited,
                                    @NotNull Faction<?> faction)
            throws PlayerHasntBeenInvitedException, PlayerNotFoundException,
            JoinWithRankInvalidException, FactionIsFrozenException,
            PlayerIsAlreadyInFactionException, PlayerIsBannedException {
        PersistentInvites.PersistentReceivedInvite invite = PersistentInvites.removeInvite(invited, faction.getRegistry());

        if (!(Rank.fromString(invite.rank()) instanceof FactionRank rank))
            throw new JoinWithRankInvalidException();

        faction.joinPlayer(invited, rank);

        faction.broadcastTranslatable(translatable -> translatable
                .getMessages()
                .getFaction()
                .getBroadcast()
                .get("invite-accepted"),
                new Placeholder("{received}", invited.getName()));

        EventExecutor.getExecutor().acceptInvite(invited,
                invite.sender(),
                faction, rank);
    }

    public static @Nullable PersistentInvites.ReceiverMap getInvites(@NotNull OfflineFactionPlayer<?> invited) {
        return PersistentInvites.getInvites(invited.getDataContainer());
    }

    public static boolean hasInvite(@NotNull OfflineFactionPlayer<?> invited,
                                    @NotNull Faction<?> faction) {
        return PersistentInvites.hasBeenInvited(invited.getDataContainer(), faction.getRegistry());
    }
}
