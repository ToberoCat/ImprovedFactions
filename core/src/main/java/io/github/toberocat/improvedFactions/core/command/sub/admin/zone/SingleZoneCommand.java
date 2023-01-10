package io.github.toberocat.improvedFactions.core.command.sub.admin.zone;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.command.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.PermissionFileTool;
import io.github.toberocat.improvedFactions.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class SingleZoneCommand extends AutoAreaCommand {

    private final Zone zone;

    public SingleZoneCommand(@NotNull Zone zone) {
        super(true);
        this.zone = zone;

        node = translatable -> translatable
                .getMessages()
                .getCommand()
                .get("zone");
        settings = createSettings();
        config = createConfig();
        PermissionFileTool.addPermission(permission(), isAdmin());
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
            ClaimHandler.protectChunk(zone.registry(),
                    world.getChunkAt(location.chunkX(), location.chunkZ()));
            if (!area) player.sendMessage(node.andThen(map -> map.get("claim-chunk")));
            return true;
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

    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return zone.alias();
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setAllowInConsole(false);
    }
}
