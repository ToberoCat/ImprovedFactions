package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.command.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.MaxArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
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

            player.sendMessage("commands.create.created-faction", new HashMap<>());
        } catch (IllegalFactionNamingException e) {
            player.sendMessage(node.andThen(map -> map.get("illegal-naming")));
        } catch (FactionAlreadyExistsException e) {
            player.sendMessage(node.andThen(map -> map.get("faction-already-exists")),
                    new Placeholder("faction", packet.display));
        } catch (FactionNotInStorage factionNotInStorage) {
            player.sendMessage(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerHasNoFactionException e) {
            player.sendMessage(node.andThen(map -> map.get("sender-has-no-faction")));
        } catch (PlayerIsAlreadyInFactionException e) {
            player.sendMessage(node.andThen(map -> map.get("sender-already-in-faction")));
        } catch (FactionIsFrozenException | PlayerIsBannedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected @Nullable List<String> getTab(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException {
        return TAB;
    }

    private @NotNull Faction<?> create(@NotNull FactionPlayer owner, @NotNull String display)
            throws IllegalFactionNamingException, FactionAlreadyExistsException,
            FactionNotInStorage, PlayerHasNoFactionException, PlayerIsAlreadyInFactionException,
            FactionIsFrozenException, PlayerIsBannedException {
        return FactionHandler.createFaction(display, owner);
    }
}
