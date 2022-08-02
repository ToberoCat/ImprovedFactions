package io.github.toberocat.core.commands.zones;

import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.command.auto.WorldAutoSubCommand;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UnclaimableZoneSubCommand extends WorldAutoSubCommand {
    public UnclaimableZoneSubCommand() {
        super("unclaimable", "zones.unclaimable",
                "command.zones.unclaimable.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    public String getEnabledKey() {
        return "command.zones.unclaimable.auto-enabled";
    }

    @Override
    public String getDisabledKey() {
        return "command.zones.unclaimable.auto-disabled";
    }

    @Override
    public List<String> suggestedRadius(@NotNull Player player, @NotNull String[] args) {
        return List.of("2", "5", "10");
    }

    @Override
    public void single(@NotNull Player player, @NotNull Chunk action) {
        try {
            ClaimManager.protectChunk(ClaimManager.UNCLAIMABLE_REGISTRY, player.getLocation().getChunk());
            Language.sendMessage("command.zones.unclaimable.claim", player);
        } catch (ChunkAlreadyClaimedException e) {
            Parser.run("command.zone.unclaimable.already-claimed")
                    .parse("{registry}", e.getRegistry())
                    .send(player);
        }
    }
}
