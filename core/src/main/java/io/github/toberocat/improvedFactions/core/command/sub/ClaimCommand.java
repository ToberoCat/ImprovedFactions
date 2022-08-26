package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.faction.components.permission.FactionPermissions;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClaimCommand extends Command<ClaimCommand.ClaimPacket, Command.ConsoleCommandPacket> {

    @Override
    public @NotNull String label() {
        return "claim";
    }

    @Override
    protected CommandSettings settings() {
        return new CommandSettings()
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermissions.CLAIM_PERMISSION);
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return null;
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return null;
    }

    @Override
    public void run(@NotNull ClaimPacket packet) {

    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable ClaimCommand.ClaimPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return null;
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }

    protected record ClaimPacket() implements CommandPacket {

    }
}
