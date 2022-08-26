package io.github.toberocat.improvedFactions.core.command.component;

import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
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
        CHUNK_MOVES.get(player.getUniqueId()).accept(player);
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
                        single(user, user.getLocation()));
                player.sendTranslatable(autoActivated());
            }
        } else {
            Location loc = player.getLocation();
            int x;
            int y = (int) loc.y();
            int z;

            Location lastLoc = null;
            for (double i = 0.0; i < 360.0; i += 0.1) {
                double angle = i * Math.PI / 180;
                x = (int) (loc.x() + packet.radius * Math.cos(angle));
                z = (int) (loc.z() + packet.radius * Math.sin(angle));
                Location now = new Location(x, y, z, loc.world());

                if (now != lastLoc)
                    single(player, now);
                lastLoc = now;
            }
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

    protected abstract void single(@NotNull FactionPlayer<?> player, @NotNull Location location);

    protected record AutoAreaPacket(@NotNull FactionPlayer<?> player, int radius,
                                    boolean auto) implements CommandPacket {

    }
}
