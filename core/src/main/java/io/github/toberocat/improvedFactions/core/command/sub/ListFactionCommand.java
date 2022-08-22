package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class ListFactionCommand extends Command<ListFactionCommand.ListPacket> {

    @Override
    public @NotNull String label() {
        return "list";
    }

    @Override
    protected CommandSettings settings() {
        return new CommandSettings()
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull ListPacket packet) {
        Consumer<String> sender = packet.receiver == null ? f ->
                Logger.api().logInfo("§e%s", f)
                : f -> packet.receiver.sendTranslatable(translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label())
                .get("entry"),
                new Placeholder("{faction}", f));

        FactionHandler.getAllFactions().forEach(sender);
    }

    @Override
    public @Nullable ListFactionCommand.ListPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                  @NotNull String[] args) {
        return new ListPacket(executor);
    }

    @Override
    public @Nullable ListFactionCommand.ListPacket createFromArgs(@NotNull String[] args) {
        return new ListPacket(null);
    }

    protected static record ListPacket(@Nullable FactionPlayer<?> receiver)
            implements CommandPacket {

    }
}
