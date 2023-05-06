package io.github.toberocat.improvedFactions.core.oldcommand.sub.utils;

import io.github.toberocat.improvedFactions.core.oldcommand.component.DepCommandSettings;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.PermissionFileTool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class BakePermissionsCommand extends Command<Command.CommandPacket, Command.ConsoleCommandPacket> {
    @Override
    public @NotNull String label() {
        return "bakePerms";
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    protected @NotNull DepCommandSettings createSettings() {
        return new DepCommandSettings(node);
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
    public void run(@NotNull Command.CommandPacket packet) {
        try {
            PermissionFileTool.bakeToFile(new File(ImprovedFactions.api().getTempFolder(),
                    "baked-permissions.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable Command.CommandPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new Command.CommandPacket() {
        };
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return new Command.ConsoleCommandPacket() {

        };
    }
}
