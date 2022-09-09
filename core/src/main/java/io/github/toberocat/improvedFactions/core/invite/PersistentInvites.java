package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PersistentInvites {
    public static void writeReceiver(@NotNull PersistentWrapper data,
                              @NotNull UUID sender,
                              @NotNull String registry,
                              @NotNull String rank) {
        ReceiverMap map = new ReceiverMap();
        map.put(registry, new PersistentReceivedInvites(sender, rank));

        data.set(PersistentHandler.RECEIVED_INVITE_KEY, map);
    }

    public static void writeSender(@NotNull PersistentWrapper data,
                              @NotNull UUID receiver) {
        List<UUID> invitesMap = new ArrayList<>();
        invitesMap.add(receiver);

        data.set(PersistentHandler.SENT_INVITE_KEY, invitesMap);
    }

    public static boolean hasBeenInvited(@NotNull PersistentWrapper senderWrapper,
                                      @NotNull String registry) {
        if (!senderWrapper.has(PersistentHandler.RECEIVED_INVITE_KEY)) return false;
        if (!(senderWrapper.get(PersistentHandler.RECEIVED_INVITE_KEY) instanceof ReceiverMap map))
            return false;

        return map.containsKey(registry);
    }

    protected static class ReceiverMap extends HashMap<String, PersistentReceivedInvites> {

    }

    protected record PersistentReceivedInvites(@NotNull UUID sender, @NotNull String rank) {

    }
}
