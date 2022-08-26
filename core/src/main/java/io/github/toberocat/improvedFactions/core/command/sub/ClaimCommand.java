package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.command.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.permission.FactionPermission;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ClaimCommand extends AutoAreaCommand {

    @Override
    public @NotNull String label() {
        return "claim";
    }

    @Override
    protected CommandSettings settings() {
        return new CommandSettings()
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermission.CLAIM_PERMISSION);
    }

    @Override
    protected Function<Translatable, String> radiusNoNumber() {
        return translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label())
                .get("radius-no-number");
    }

    @Override
    protected Function<Translatable, String> autoActivated() {
        return translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label())
                .get("auto-activated");
    }

    @Override
    protected Function<Translatable, String> autoDisabled() {
        return translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label())
                .get("auto-disabled");
    }

    @Override
    protected void single(@NotNull FactionPlayer<?> player, @NotNull Location location) {
        World<?> world = ImprovedFactions.api().getWorld(location.world());
        if (world == null) return;

        try {
            Faction<?> faction = player.getFaction();
            ClaimHandler.protectChunk(faction.getRegistry(),
                    world.getChunkAt(location.chunkX(), location.chunkZ()));

            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("claim-chunk"));
        } catch (FactionNotInStorage e) {
            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("faction-not-in-storage"));
        } catch (PlayerHasNoFactionException e) {
            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("player-has-no-faction"));
        } catch (ChunkAlreadyClaimedException e) {
            player.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("chunk-already-claimed"));
        }
    }
}
