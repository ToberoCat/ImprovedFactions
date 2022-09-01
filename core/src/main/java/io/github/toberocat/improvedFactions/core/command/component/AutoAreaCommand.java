package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.task.AsyncTask;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AutoAreaCommand extends
        Command<AutoAreaCommand.AutoAreaPacket, Command.ConsoleCommandPacket> {

    private static final Map<UUID, Consumer<FactionPlayer<?>>> CHUNK_MOVES = new HashMap<>();

    public static void move(@NotNull FactionPlayer<?> player) {
        Consumer<FactionPlayer<?>> consumer = CHUNK_MOVES.get(player.getUniqueId());
        if (consumer == null) return;

        consumer.accept(player);
    }

    @Override
    public final @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return List.of("auto", "1", "2", "5");
    }

    @Override
    public final @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public final void run(@NotNull AutoAreaPacket packet) {
        FactionPlayer<?> player = packet.player;
        if (packet.auto) {
            if (CHUNK_MOVES.containsKey(player.getUniqueId())) {
                CHUNK_MOVES.remove(player.getUniqueId());
                player.sendTranslatable(autoDisabled());
            } else {
                CHUNK_MOVES.computeIfAbsent(player.getUniqueId(), id -> user ->
                        single(user, user.getLocation(), false));
                player.sendTranslatable(autoActivated());
            }
        } else {
            new AsyncTask<>(() -> {
                Location loc = player.getLocation();

                int maxRadius = config().getInt("max-radius", 5);
                int radius = Math.min(packet.radius, maxRadius);
                if (radius == 1) {
                    int success = single(player, loc, false) ? 1 : 0;
                    return new AsyncClaimPacket(success, 1 - success);
                } else {
                    radius /= 2;
                    int success = 0, fail = 0;
                    for (int i = -radius; i <= radius; i++) {
                        for (int j = -radius; j <= radius; j++) {
                            Location now = new Location(loc.x() + i * 16, loc.y(), loc.z() + j * 16, loc.world());
                            if (single(player, now, true)) success++;
                            else fail++;
                        }
                    }

                    return new AsyncClaimPacket(success, fail);
                }
            }).start().then(claim -> player.sendTranslatable(sendTotal(),
                    new Placeholder("{success}", String.valueOf(claim.success)),
                    new Placeholder("{total}", String.valueOf(claim.success + claim.fail))
            ));
        }
    }

    @Override
    public final void runConsole(@NotNull ConsoleCommandPacket packet) {
        Logger.api().logInfo("Can't run area auto commands in console");
    }

    @Override
    public final @Nullable AutoAreaPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        if (args.length == 0) return new AutoAreaPacket(executor, 1, false);

        String radius = args[0];
        if (radius.equals("auto")) return new AutoAreaPacket(executor, 1, true);

        try {
            return new AutoAreaPacket(executor, Integer.parseInt(radius), false);
        } catch (NumberFormatException e) {
            executor.sendTranslatable(radiusNoNumber());
            return null;
        }
    }

    @Override
    public final @Nullable ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }

    protected abstract Function<Translatable, String> radiusNoNumber();

    protected abstract Function<Translatable, String> autoActivated();

    protected abstract Function<Translatable, String> autoDisabled();

    protected abstract boolean single(@NotNull FactionPlayer<?> player, @NotNull Location location, boolean area);

    protected abstract Function<Translatable, String> sendTotal();

    protected record AutoAreaPacket(@NotNull FactionPlayer<?> player, int radius,
                                    boolean auto) implements CommandPacket {

    }

    private record AsyncClaimPacket(int success, int fail) {

    }
}
