/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.command.sub.faction;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.gui.provided.SettingGui;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SettingCommand extends Command<SettingCommand.SettingPacket, Command.ConsoleCommandPacket> {

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "settings";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermission.OPEN_SETTINGS_PERMISSION);
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
    public void run(@NotNull SettingPacket packet) {
        GuiManager.openGui(SettingGui.GUI_ID, packet.factionPlayer);
    }

    @Override
    public void runConsole(@NotNull Command.ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable SettingCommand.SettingPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new SettingPacket(executor);
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return new Command.ConsoleCommandPacket() {
        };
    }

    public Function<Translatable, Map<String, String>> getNode() {
        return node;
    }

    protected record SettingPacket(@NotNull FactionPlayer<?> factionPlayer) implements Command.CommandPacket {

    }


}
