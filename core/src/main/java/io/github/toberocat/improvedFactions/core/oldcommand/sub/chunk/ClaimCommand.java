package io.github.toberocat.improvedFactions.core.oldcommand.sub.chunk;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.oldcommand.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.oldcommand.component.DepCommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class ClaimCommand extends AutoAreaCommand {

    public static final String LABEL = "claim";
    private static final Function<Translatable, Map<String, String>> node = translatable -> translatable
            .getMessages()
            .getCommand()
            .get(LABEL);

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    protected @NotNull DepCommandSettings createSettings() {
        return new DepCommandSettings(node)
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermission.CLAIM_PERMISSION);
    }

    @Override
    protected Function<Translatable, String> radiusNoNumber() {
        return node.andThen(map -> map.get("radius-no-number"));
    }

    @Override
    protected Function<Translatable, String> autoActivated() {
        return node.andThen(map -> map.get("auto-activated"));
    }

    @Override
    protected Function<Translatable, String> autoDisabled() {
        return node.andThen(map -> map.get("auto-disabled"));
    }

    @Override
    protected boolean single(@NotNull FactionPlayer<?> player, @NotNull Location location, boolean area) {
        World<?> world = ImprovedFactions.api().getWorld(location.world());
        if (world == null) return false;

        try {
            Faction<?> faction = player.getFaction();
            ClaimHandler.protectChunk(faction.getRegistry(),
                    world.getChunkAt(location.chunkX(), location.chunkZ()));

            if (!area) player.sendMessage(node.andThen(map -> map.get("claim-chunk")));
            return true;
        } catch (FactionNotInStorage e) {
            player.sendMessage(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerHasNoFactionException e) {
            player.sendMessage(node.andThen(map -> map.get("sender-has-no-faction")));
        } catch (ChunkAlreadyClaimedException e) {
            if (area) return false;
            player.sendMessage(node.andThen(map -> map.get("chunk-already-claimed")));
        }
        return false;
    }

    @Override
    protected Function<Translatable, String> sendTotal() {
        return node.andThen(map -> map.get("total-area"));
    }
}
