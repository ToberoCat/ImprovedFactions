package io.github.toberocat.improvedFactions.core.command.sub.admin.gui;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiImplementation;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EditGuiCommand extends Command<EditGuiCommand.EditorGuiPacket, EditGuiCommand.EditorGuiPacket> {
    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "edit";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setAllowInConsole(false);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return GuiManager.getGuis();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return GuiManager.getGuis();
    }

    @Override
    public void run(@NotNull EditorGuiPacket packet) {
        GuiImplementation editor = ImplementationHolder.guiImplementation;
        if (editor == null) {
            packet.sender.sendMessage("Editor hasn't been set yet");
            return;
        }

        editor.openEditor(packet.sender, packet.guiId);
    }

    @Override
    public void runConsole(@NotNull EditorGuiPacket packet) {

    }


    @Override
    public @Nullable EditorGuiPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                    @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendFancyMessage(node.andThen(map -> map.get("not-enough-arguments")));
            return null;
        }
        String guiId = args[0];
        if (!GuiManager.getGuis().contains(guiId)) {
            executor.sendFancyMessage(node.andThen(map -> map.get("gui-couldnt-be-found")));
            return null;
        }

        return new EditorGuiPacket(executor, args[0]);
    }

    @Override
    public @Nullable EditorGuiPacket createFromArgs(@NotNull String[] args) {
        if (args.length != 1) {
            Logger.api().logInfo("You haven't given a valid guiId");
            return null;
        }
        String guiId = args[0];
        if (!GuiManager.getGuis().contains(guiId)) {
            Logger.api().logInfo("The given gui id wasn't found in the registry");
            return null;
        }

        return new EditorGuiPacket(ImprovedFactions.api().getConsoleSender(), args[0]);
    }

    protected record EditorGuiPacket(@NotNull CommandSender sender, @NotNull String guiId)
            implements Command.CommandPacket, ConsoleCommandPacket {

    }
}
