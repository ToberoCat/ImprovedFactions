package io.github.toberocat.improvedFactions.core.oldcommand.sub.faction.settings;

import io.github.toberocat.improvedFactions.core.oldcommand.component.DepCommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class PowerCommand extends Command<Command.PlayerPacket, PowerCommand.PowerConsolePacket> {

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "power";
    }

    @Override
    protected @NotNull DepCommandSettings createSettings() {
        return new DepCommandSettings(node)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermission.VIEW_POWER);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return FactionHandler.getAllFactions().toList();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull PlayerPacket packet) {
        packet.player().sendMessage(node.andThen(map -> map.get("get-power")));
    }

    @Override
    public void runConsole(@NotNull PowerConsolePacket packet) {
        Logger.api().logInfo("" + packet.faction.getTotalPower());
    }

    @Override
    public @Nullable Command.PlayerPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new PlayerPacket(executor);
    }

    @Override
    public @Nullable PowerCommand.PowerConsolePacket createFromArgs(@NotNull String[] args) {
        if (args.length <= 1) {
            Logger.api().logInfo("You need to specify a faction");
            return null;
        }

        try {
            return new PowerConsolePacket(FactionHandler.getFaction(args[0]));
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction not found");
            return null;
        }
    }

    protected record PowerConsolePacket(Faction<?> faction) implements ConsoleCommandPacket {

    }
}
