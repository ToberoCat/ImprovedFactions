package io.github.toberocat.improvedFactions.core.oldcommand.sub.admin.zone;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.oldcommand.component.DepCommandSettings;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ZoneRootCommand extends
        Command<Command.CommandPacket, Command.ConsoleCommandPacket> {

    public ZoneRootCommand() {
        ClaimHandler.getZones().forEach(registry -> {
            Zone zone = ClaimHandler.getZone(registry);
            if (zone == null) return;

            add(new SingleZoneCommand(zone));
        });
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "zones";
    }

    @Override
    protected @NotNull DepCommandSettings createSettings() {
        return new DepCommandSettings(node)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull Command.CommandPacket packet) {
    }

    @Override
    public void runConsole(@NotNull Command.ConsoleCommandPacket packet) {

    }

    @Nullable
    @Override
    public ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }

    @Nullable
    @Override
    public CommandPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                        @NotNull String[] args) {
        return null;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return Collections.emptyList();
    }
}
