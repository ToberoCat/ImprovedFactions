package io.github.toberocat.improvedFactions.core.command.sub.faction;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.*;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CreateFactionCommand extends
        Command<CreateFactionCommand.CreateFactionPacket, CreateFactionCommand.CreateFactionPacket> {

    public static final String LABEL = "create";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(true)
                .setRequiresNoFaction(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return List.of("name");
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return ImprovedFactions.api()
                .listPlayers()
                .getOnlinePlayerNames()
                .toList();

        if (args.length <= 2) return List.of("name");

        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull CreateFactionPacket packet) {
        FactionPlayer<?> owner = packet.owner;
        if (!owner.hasPermission(permission())) {
            owner.sendTranslatable(node.andThen(map -> map.get("not-enough-permissions")));
            return;
        }

        try {
            Faction<?> faction = create(owner, packet.display);

            owner.sendTranslatable(node.andThen(map -> map.get("created-faction")),
                    new Placeholder("{faction}", faction.getDisplay()));
        } catch (IllegalFactionNamingException e) {
            owner.sendTranslatable(node.andThen(map -> map.get("illegal-naming")));
        } catch (FactionAlreadyExistsException e) {
            owner.sendTranslatable(node.andThen(map -> map.get("faction-already-exists")));
        } catch (FactionNotInStorage factionNotInStorage) {
            owner.sendTranslatable(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerHasNoFactionException e) {
            owner.sendTranslatable(node.andThen(map -> map.get("player-has-no-faction")));
        } catch (PlayerIsAlreadyInFactionException e) {
            owner.sendTranslatable(node.andThen(map -> map.get("player-already-in-faction")));
        } catch (FactionIsFrozenException | PlayerIsBannedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runConsole(@NotNull CreateFactionPacket packet) {
        FactionPlayer<?> owner = packet.owner;

        Logger logger = Logger.api();
        try {
            Faction<?> faction = create(owner, packet.display);
            owner.sendTranslatable(node.andThen(map -> map.get("created-faction")),
                    new Placeholder("{faction}", faction.getDisplay()));

            logger.logInfo("You created faction %s for %s",
                    faction.getDisplay(), owner.getName());
        } catch (IllegalFactionNamingException e) {
            logger.logInfo("You can't name the faction %s. This name isn't allowed", packet.display);
        } catch (FactionAlreadyExistsException e) {
            logger.logInfo("You can't name another faction %s." +
                    " This name is in use", packet.display);
        } catch (FactionNotInStorage factionNotInStorage) {
            logger.logInfo("Faction can't be found on disk");
        } catch (PlayerHasNoFactionException e) {
            logger.logInfo("Player has no faction");
        } catch (PlayerIsAlreadyInFactionException e) {
            logger.logInfo("Player %s is already in a faction", owner.getName());
        } catch (FactionIsFrozenException | PlayerIsBannedException e) {
            e.printStackTrace();
        }
    }

    private @NotNull Faction<?> create(@NotNull FactionPlayer<?> owner, @NotNull String display)
            throws IllegalFactionNamingException, FactionAlreadyExistsException,
            FactionNotInStorage, PlayerHasNoFactionException, PlayerIsAlreadyInFactionException,
            FactionIsFrozenException, PlayerIsBannedException {
        if (owner.inFaction()) throw new PlayerIsAlreadyInFactionException(owner.getFaction(), owner);
        return FactionHandler.createFaction(display, owner);
    }

    @Override
    @Nullable
    public CreateFactionCommand.CreateFactionPacket createFromArgs(@NotNull FactionPlayer<?> sender,
                                                                             @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendTranslatable(node.andThen(map -> map.get("not-enough-arguments")));
            return null;
        }

        String display = args[0];
        return new CreateFactionPacket(display, sender);
    }

    @Override
    public @Nullable CreateFactionCommand.CreateFactionPacket createFromArgs(@NotNull String[] args) {
        if (args.length != 2) {
            Logger.api().logInfo("You need to specify the player who creates a " +
                    "faction, as well as it's name");
            return null;
        }

        String player = args[0];
        ImprovedFactions<?> api = ImprovedFactions.api();
        if (api.listPlayers()
                .getOnlinePlayerNames()
                .noneMatch(x -> x.equals(player))) {
            Logger.api().logInfo("Couldn't find a online player with this name");
            return null;
        }

        FactionPlayer<?> executor = api.getPlayer(player);
        if (executor == null) {
            Logger.api().logInfo("Couldn't find a online player with this name");
            return null;
        }

        String display = args[1];
        return new CreateFactionPacket(display, executor);
    }

    protected record CreateFactionPacket(@NotNull String display,
                                      @NotNull FactionPlayer<?> owner)
            implements CommandPacket, ConsoleCommandPacket {
    }
}
