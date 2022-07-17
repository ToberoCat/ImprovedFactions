package io.github.toberocat.core.invite;

import io.github.toberocat.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class Invite {
    private final UUID inviteId;
    private final OfflinePlayer receiver;

    public Invite(@NotNull UUID inviteId,
                  @NotNull OfflinePlayer receiver,
                  long duration) {
        this.inviteId = inviteId;
        this.receiver = receiver;

        Bukkit.getScheduler().runTaskLater(MainIF.getIF(), this::cancelInvite, duration);
    }

    public void cancelInvite() {
    }

    public UUID getInviteId() {
        return inviteId;
    }

    public OfflinePlayer getReceiver() {
        return receiver;
    }
}
