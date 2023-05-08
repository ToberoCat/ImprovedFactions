package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasntBeenInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.player.PlayerNotFoundException;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class PersistentInvites {
    public static void writeReceiver(@NotNull PersistentWrapper data,
                              @NotNull UUID sender,
                              @NotNull String registry,
                              @NotNull String rank) {
        ReceiverMap map = new ReceiverMap();
        map.put(registry, new PersistentReceivedInvite(sender, rank));

        data.set(PersistentHandler.RECEIVED_INVITE_KEY, map);
    }

    public static void writeSender(@NotNull PersistentWrapper data,
                              @NotNull UUID receiver) {
        InviteList invitesMap = new InviteList();
        invitesMap.add(receiver);

        data.set(PersistentHandler.SENT_INVITE_KEY, invitesMap);
    }

    public static @Nullable ReceiverMap getInvites(@NotNull PersistentWrapper receiverWrapper) {
        if (!receiverWrapper.has(PersistentHandler.RECEIVED_INVITE_KEY)) return null;
        return receiverWrapper.get(PersistentHandler.RECEIVED_INVITE_KEY) instanceof ReceiverMap invites
                ? invites : null;
    }

    public static @NotNull PersistentReceivedInvite removeInvite(@NotNull OfflineFactionPlayer receiver,
                                    @NotNull String registry)
            throws PlayerHasntBeenInvitedException, PlayerNotFoundException {
        PersistentWrapper receiverWrapper = receiver.getDataContainer();
        ReceiverMap invites = getInvites(receiverWrapper);
        if (invites == null) throw new PlayerHasntBeenInvitedException();

        PersistentReceivedInvite invite = invites.remove(registry);
        if (invite == null) throw new PlayerHasntBeenInvitedException();

        receiverWrapper.set(PersistentHandler.RECEIVED_INVITE_KEY, invites);
        OfflineFactionPlayer sender = ImprovedFactions.api().getOfflinePlayer(invite.sender);
        if (sender == null) throw new PlayerNotFoundException();

        PersistentWrapper senderData = sender.getDataContainer();
        if (!senderData.has(PersistentHandler.SENT_INVITE_KEY) ||
                !(senderData.get(PersistentHandler.SENT_INVITE_KEY) instanceof InviteList list))
            return invite;

        list.remove(receiver.getUniqueId());
        senderData.set(PersistentHandler.SENT_INVITE_KEY, list);

        return invite;
    }

    public static boolean hasBeenInvited(@NotNull PersistentWrapper receiverWrapper,
                                      @NotNull String registry) {
        if (!receiverWrapper.has(PersistentHandler.RECEIVED_INVITE_KEY)) return false;
        if (!(receiverWrapper.get(PersistentHandler.RECEIVED_INVITE_KEY) instanceof ReceiverMap map))
            return false;

        return map.containsKey(registry);
    }

    public static class ReceiverMap extends HashMap<String, PersistentReceivedInvite> {

    }

    public static class InviteList extends ArrayList<UUID> {

    }

    public record PersistentReceivedInvite(@NotNull UUID sender, @NotNull String rank) {

    }
}
