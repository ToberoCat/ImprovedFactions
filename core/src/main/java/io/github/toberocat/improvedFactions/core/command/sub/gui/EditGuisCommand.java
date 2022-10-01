package io.github.toberocat.improvedFactions.core.command.sub.gui;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.gui.GuiImplementation;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class EditGuisCommand extends Command<EditGuisCommand.EditorGuiPacket, Command.ConsoleCommandPacket> {
    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "editGuis";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setAllowInConsole(false);
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
    public void run(@NotNull EditorGuiPacket packet) {
        GuiImplementation editor = ImplementationHolder.guiImplementation;
        if (editor == null) {
            packet.player.sendMessage("Editor hasn't been set yet");
            return;
        }

        editor.openEditor(packet.player);
    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable EditorGuiPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new EditorGuiPacket(executor);
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return new ConsoleCommandPacket() {
        };
    }

    protected record EditorGuiPacket(@NotNull FactionPlayer<?> player) implements CommandPacket {

    }
}
