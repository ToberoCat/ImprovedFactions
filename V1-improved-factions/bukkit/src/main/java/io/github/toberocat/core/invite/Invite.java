package io.github.toberocat.core.invite;

import io.github.toberocat.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class Invite {

    protected final UUID inviteId;
    protected final UUID receiver;
    protected final int taskId;

    public Invite(@NotNull UUID inviteId,
                  @NotNull UUID receiver,
                  long duration) {
        this.inviteId = inviteId;
        this.receiver = receiver;

        taskId = Bukkit.getScheduler().runTaskLater(MainIF.getIF(), this::ranOutOfTime, duration).getTaskId();
    }

    public abstract void ranOutOfTime();

    public abstract void cancelInvite(@NotNull Player player);

    public abstract void accept(@NotNull Player player);

    protected void cancelTask() {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public UUID getInviteId() {
        return inviteId;
    }

    public UUID getReceiver() {
        return receiver;
    }
}
