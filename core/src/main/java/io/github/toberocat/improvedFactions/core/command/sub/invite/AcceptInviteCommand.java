package io.github.toberocat.improvedFactions.core.command.sub.invite;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AcceptInviteCommand extends Command<AcceptInviteCommand.AcceptPacket,
        AcceptInviteCommand.AcceptPacket> {

    public static final String LABEL = "acceptinvite";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(false)
                .setRequiresNoFaction(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
    }

    @Override
    public void run(@NotNull AcceptPacket packet) {

    }

    @Override
    public void runConsole(@NotNull AcceptPacket packet) {

    }

    @Override
    public @Nullable AcceptInviteCommand.AcceptPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return null;
    }

    @Override
    public @Nullable AcceptInviteCommand.AcceptPacket createFromArgs(@NotNull String[] args) {
        return null;
    }


    protected record AcceptPacket(@NotNull Faction<?> faction,
                                  @NotNull OfflineFactionPlayer<?> receiver)
            implements CommandPacket, ConsoleCommandPacket {

    }
}
