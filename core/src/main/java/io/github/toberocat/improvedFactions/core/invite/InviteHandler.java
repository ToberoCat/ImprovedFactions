package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasBeenInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasntBeenInvitedException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class InviteHandler {

    private static final PlayerHasBeenInvitedException HAS_BEEN_INVITED = new PlayerHasBeenInvitedException();
    private static final PlayerHasntBeenInvitedException NOT_INVITED_EXCEPTION = new PlayerHasntBeenInvitedException();

    public static void sendInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                  @NotNull FactionPlayer<?> sender,
                                  @NotNull Faction<?> faction,
                                  @NotNull FactionRank rank) throws PlayerHasBeenInvitedException {
        if (hasInvite(receiver, faction)) throw HAS_BEEN_INVITED;
        receiver.getDataContainer()
                .set(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry(), String
                        .format("sender=%s;registry=%s;rank=%s",
                                sender.getUniqueId(),
                                faction.getRegistry(),
                                rank.getRegistry()));

        sender.getDataContainer()
                .set(PersistentHandler.SENT_INVITE_KEY + "_" + faction.getRegistry(), String
                        .format("receiver=%s", sender.getUniqueId()));

        faction.broadcastTranslatable(translatable -> translatable
                        .getMessages()
                        .getFaction()
                        .getBroadcast()
                        .get("invite-sent"),
                new Placeholder("{sender}", sender.getName()),
                new Placeholder("{receiver}", receiver.getName()));

        EventExecutor.getExecutor().invitePlayer(receiver, sender, faction, rank);
    }

    public static void cancelInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                    @NotNull Faction<?> faction)
            throws PlayerHasntBeenInvitedException {
        String entry = receiver.getDataContainer()
                .get(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry());
        if (entry == null) throw NOT_INVITED_EXCEPTION;

        ReceivedInvites invites = parseReceived(receiver, entry);
        if (invites == null) return;

        receiver.getDataContainer()
                        .remove(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry());
        invites.sender().getDataContainer()
                .remove(PersistentHandler.SENT_INVITE_KEY + "_" + faction.getRegistry());

        invites.faction().broadcastTranslatable(translatable -> translatable
                .getMessages()
                .getFaction()
                .getBroadcast()
                .get("invite-cancelled"));

        EventExecutor.getExecutor().cancelInvite(receiver,
                invites.sender(),
                faction, invites.rank());
    }

    public static void acceptInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                    @NotNull Faction<?> faction) throws PlayerHasntBeenInvitedException {
        String entry = receiver.getDataContainer()
                .get(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry());
        if (entry == null) throw NOT_INVITED_EXCEPTION;

        ReceivedInvites invites = parseReceived(receiver, entry);
        if (invites == null) return;

        receiver.getDataContainer()
                .remove(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry());
        invites.sender().getDataContainer()
                .remove(PersistentHandler.SENT_INVITE_KEY + "_" + faction.getRegistry());


        invites.faction().broadcastTranslatable(translatable -> translatable
                .getMessages()
                .getFaction()
                .getBroadcast()
                .get("invite-accepted"));

        EventExecutor.getExecutor().acceptInvite(receiver,
                invites.sender(),
                faction, invites.rank());
    }

    public static boolean hasInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                    @NotNull Faction<?> faction) {
        String entry = receiver.getDataContainer()
                .get(PersistentHandler.RECEIVED_INVITE_KEY + "_" + faction.getRegistry());
        return entry == null;
    }

    private static @Nullable ReceivedInvites parseReceived(@NotNull OfflineFactionPlayer<?> receiver,
                                                           @NotNull String str) {
        String[] part = str.split(";");
        if (part.length == 3) return null;

        //ToDo: Check length of parts before actually using it
        UUID senderUuid = UUID.fromString(part[0].split("=")[1]);
        String registry = part[1].split("=")[1];
        String rankId = part[2].split("=")[1];

        ImprovedFactions<?> api = ImprovedFactions.api();
        OfflineFactionPlayer<?> sender = api.getOfflinePlayer(senderUuid);
        if (sender == null) return null;

        try {
            Faction<?> faction = FactionHandler.getFaction(registry);
            if (!(Rank.fromString(rankId) instanceof FactionRank rank)) return null;

            return new ReceivedInvites(receiver, sender, faction, rank);
        } catch (FactionNotInStorage e) {
            return null;
        }
    }
}
