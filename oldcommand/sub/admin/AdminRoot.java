package io.github.toberocat.improvedFactions.core.oldcommand.sub.admin;

import io.github.toberocat.improvedFactions.core.oldcommand.component.DepCommandSettings;
import io.github.toberocat.improvedFactions.core.oldcommand.sub.admin.gui.GuiCommand;
import io.github.toberocat.improvedFactions.core.oldcommand.sub.admin.zone.ZoneRootCommand;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AdminRoot extends Command<Command.CommandPacket, Command.ConsoleCommandPacket> {

    public AdminRoot() {
        add(new GuiCommand());
        add(new ZoneRootCommand());
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "admin";
    }

    @Override
    protected @NotNull DepCommandSettings createSettings() {
        return new DepCommandSettings(node)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull CommandPacket packet) {

    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable Command.CommandPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return null;
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }
}
