/**
 * Created: 30/09/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.command.sub.admin.gui;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class ItemTranslatableCommand
        extends Command<ItemTranslatableCommand.TranslatePacket, Command.ConsoleCommandPacket> {

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "translate";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setAllowInConsole(false);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return List.of("name");
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull TranslatePacket packet) {
        ItemStack item = packet.player.getMainItem();
        item.setName(packet.itemName);

        packet.player.sendTranslatable(node.andThen(map -> map.get("set-name")));
    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable ItemTranslatableCommand.TranslatePacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                            @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendTranslatable(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }

        return new TranslatePacket(executor, args[0]);
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return null;
    }

    protected record TranslatePacket(@NotNull FactionPlayer<?> player,
                                     @NotNull String itemName)
            implements CommandPacket {

    }
}
