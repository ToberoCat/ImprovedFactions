package io.github.toberocat.improvedFactions.core.invite;

import io.github.toberocat.improvedFactions.core.event.EventExecutor;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.task.AsyncTask;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;

public class InviteHandler {

    public static void sendInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                  @NotNull FactionPlayer<?> sender,
                                  @NotNull Faction<?> faction,
                                  @NotNull FactionRank rank) {
        receiver.getDataContainer()
                .set(PersistentHandler.RECEIVED_INVITE_KEY, String
                        .format("sender=%s;registry=%s;rank=%s",
                                sender.getUniqueId(),
                                faction.getRegistry(),
                                rank.getRegistry()));

        sender.getDataContainer()
                .set(PersistentHandler.SENT_INVITE_KEY, String
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
                                    @NotNull Faction<?> faction) {
        String entry = receiver.getDataContainer()
                .get(PersistentHandler.RECEIVED_INVITE_KEY);

    }

    public static void acceptInvite(@NotNull Faction<?> faction) {

    }

    public static boolean hasInvite(@NotNull OfflineFactionPlayer<?> receiver,
                                    @NotNull Faction<?> faction) {

    }
}
