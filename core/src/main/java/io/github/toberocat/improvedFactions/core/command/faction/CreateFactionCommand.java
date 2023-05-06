package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.MaxArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CreateFactionCommand extends PlayerSubCommand {

    public static final List<String> TAB = List.of("faction-name");

    public CreateFactionCommand() {
        super("create", Options.getFromConfig("create")
                .opt(new FactionOption(true))
                .opt(new ArgLengthOption(1))
                .opt(new MaxArgLengthOption(0, Faction.MAX_FACTION_DISPLAY_LENGTH)));
    }

    @Override
    protected boolean handle(@NotNull FactionPlayer player, @NotNull String[] args) throws CommandException {
        try {
            Faction<?> faction = create(player, args[0]);
            player.sendMessage("commands.create.created-faction", new PlaceholderBuilder()
                            .placeholder("faction", faction)
                    .getPlaceholders());
        } catch (TranslatableException e) {
            player.sendMessage(e.getTranslationKey(), e.getPlaceholders());
            return false;
        }
        return true;
    }

    @Override
    protected @Nullable List<String> getTab(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException {
        return TAB;
    }

    private @NotNull Faction<?> create(@NotNull FactionPlayer owner, @NotNull String display)
            throws TranslatableException {
        return FactionHandler.createFaction(display, owner);
    }
}
