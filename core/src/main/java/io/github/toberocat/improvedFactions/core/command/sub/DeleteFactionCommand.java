package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsNotOwnerException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class DeleteFactionCommand extends Command<DeleteFactionCommand.DeleteFactionPacket> {

    @Override
    public @NotNull String label() {
        return "delete";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull DeleteFactionPacket packet) {
        FactionPlayer<?> player = packet.owner;
        try {
            delete(player);
        } catch (FactionNotInStorage e) {
            player.sendTranslatable(translatable -> translatable.getMessages().getCommand()
                    .get(label())
                    .get("faction-not-in-storage"));
        } catch (PlayerHasNoFactionException e) {
            player.sendTranslatable(translatable -> translatable.getMessages().getCommand()
                    .get(label())
                    .get("player-has-no-faction"));
        } catch (FactionIsFrozenException e) {
            player.sendTranslatable(translatable -> translatable.getMessages().getCommand()
                    .get(label())
                    .get("faction-frozen"));
        } catch (PlayerIsNotOwnerException e) {
            player.sendTranslatable(translatable -> translatable.getMessages().getCommand()
                    .get(label())
                    .get("player-needs-to-be-owner"));
        }
    }

    private void delete(@NotNull FactionPlayer<?> player) throws FactionNotInStorage,
            PlayerHasNoFactionException, FactionIsFrozenException, PlayerIsNotOwnerException {
        Faction<?> faction = player.getFaction();
        if (!faction.getOwner().equals(player.getUniqueId())) throw new PlayerIsNotOwnerException(faction, player);

        faction.deleteFaction();
    }

    @Override
    public @Nullable DeleteFactionCommand.DeleteFactionPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                             @NotNull String[] args) {
        return new DeleteFactionPacket(executor);
    }

    public record DeleteFactionPacket(@NotNull FactionPlayer<?> owner)
            implements CommandPacket {
    }
}
