package io.github.toberocat.core.invite;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InviteHandler {
    private static Map<UUID, Invite> invites = new HashMap<>();
    private static Map<UUID, ArrayList<UUID>> playerInvites = new HashMap<>();

    public static void createInvite(@NotNull Player player, @NotNull Invite invite) {
        //ToDo: Create a invite based of the data
    }

    public static @Nullable Invite getInviteById(@NotNull UUID inviteId) {
        return invites.get(inviteId);
    }

    public static @Nullable ArrayList<UUID> getPlayerInvites(@NotNull Player player) {
        return playerInvites.get(player.getUniqueId());
    }

    public static boolean wantsInvites(@NotNull Player player) {
        //ToDo: PlayerSettings - Allow changing of receiving invtes / doing it not
        return true;
    }
}
