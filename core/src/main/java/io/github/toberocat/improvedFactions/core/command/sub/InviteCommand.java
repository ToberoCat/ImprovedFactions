package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InviteCommand extends Command<InviteCommand.InvitePacket, InviteCommand.InvitePacket> {

    public static final String LABEL = "invite";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiresFaction(true)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return null;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return null;
    }

    @Override
    public void run(@NotNull InvitePacket packet) {

    }

    @Override
    public void runConsole(@NotNull InvitePacket packet) {

    }

    @Override
    public @Nullable InviteCommand.InvitePacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return null;
    }

    @Override
    public @Nullable InviteCommand.InvitePacket createFromArgs(@NotNull String[] args) {
        return null;
    }

    protected record InvitePacket(@NotNull FactionPlayer<?> sender,
                                  @NotNull OfflineFactionPlayer<?> receiver,
                                  @NotNull Faction<?> faction,
                                  @NotNull FactionRank rank)
            implements CommandPacket, ConsoleCommandPacket {
    }

}
