package io.github.toberocat.improvedFactions.core.utils;

import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CooldownException;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created: 08/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class CooldownManager {
    private static final @NotNull SimpleDateFormat defaultFormat =
            new SimpleDateFormat("m'm'ss's'");
    private final @NotNull Map<UUID, Long> cooldowns;
    private final @NotNull SimpleDateFormat format;
    private final long cooldownMs, cooldownTicks;

    private CooldownManager(@NotNull SimpleDateFormat format, long cooldownTicks) {
        this.cooldowns = new HashMap<>();
        this.cooldownTicks = cooldownTicks;
        this.cooldownMs = cooldownTicks * 50;
        this.format = format;
    }

    public static @NotNull CooldownManager createManager(@NotNull TimeUnit timeUnit,
                                                         long duration) {
        return createManager(defaultFormat, timeUnit, duration);
    }

    public static @NotNull CooldownManager createManager(@NotNull SimpleDateFormat format,
                                                         @NotNull TimeUnit timeUnit,
                                                         long duration) {
        return new CooldownManager(format, timeUnit.toSeconds(duration) * 20);
    }

    public void activateCooldown(@NotNull UUID player) {
        cooldowns.put(player, System.currentTimeMillis() + cooldownMs);
        ImprovedFactions.api().getScheduler().runLater(() -> cooldowns.remove(player), cooldownTicks);
    }

    public void removeCooldown(@NotNull UUID player) {
        cooldowns.remove(player);
    }

    public @NotNull Optional<Long> untilInCooldown(@NotNull UUID player) {
        return Optional.ofNullable(cooldowns.get(player));
    }

    public long getLeftCooldown(@NotNull UUID player) {
        return untilInCooldown(player).orElse(System.currentTimeMillis())
                - System.currentTimeMillis();
    }

    public @NotNull String getLeftTime(@NotNull UUID player) {
        return format.format(getLeftCooldown(player));
    }

    public void runCooldown(@NotNull UUID player) throws CooldownException {
        if (hasCooldown(player))
            throw new CooldownException(this, player);
        activateCooldown(player);
    }

    public boolean hasCooldown(@NotNull UUID player) {
        return cooldowns.containsKey(player);
    }
}
