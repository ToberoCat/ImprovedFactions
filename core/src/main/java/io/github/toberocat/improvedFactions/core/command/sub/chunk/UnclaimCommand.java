package io.github.toberocat.improvedFactions.core.command.sub.chunk;

import io.github.toberocat.improvedFactions.core.command.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionDoesntOwnChunkException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.permission.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class UnclaimCommand extends AutoAreaCommand {

    public static final String LABEL = "unclaim";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setAllowInConsole(false)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setRequiredFactionPermission(FactionPermission.UNCLAIM_PERMISSION);
    }

    @Override
    protected boolean single(@NotNull FactionPlayer<?> player, @NotNull Location location, boolean area) {
        World<?> world = ImprovedFactions.api().getWorld(location.world());
        if (world == null) return false;

        try {
            Faction<?> faction = player.getFaction();
            faction.getClaims()
                    .getClaim(world.getChunkAt(location.chunkX(), location.chunkZ()))
                    .unclaim();

            if (!area) player.sendTranslatable(node.andThen(map -> map.get("unclaim-chunk")));
            return true;
        } catch (FactionNotInStorage e) {
            player.sendTranslatable(node.andThen(map -> map.get("faction-not-in-storage")));
        } catch (PlayerHasNoFactionException e) {
            player.sendTranslatable(node.andThen(map -> map.get("player-has-no-faction")));
        } catch (FactionDoesntOwnChunkException e) {
            if (area) return false;

            player.sendTranslatable(node.andThen(map -> map.get("chunk-not-yours")),
                    new Placeholder("{claim}", e.getActualClaim() == null ? "§2wilderness" : e.getActualClaim()));
        }
        return false;
    }

    @Override
    protected Function<Translatable, String> sendTotal() {
        return node.andThen(map -> map.get("total-area"));
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
}
