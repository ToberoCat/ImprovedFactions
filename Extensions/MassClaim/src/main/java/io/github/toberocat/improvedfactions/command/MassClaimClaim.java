package io.github.toberocat.improvedfactions.command;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.Cuboid;
import io.github.toberocat.improvedfactions.Position;
import io.github.toberocat.improvedfactions.wand.PositionWand;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.github.toberocat.improvedfactions.MassClaimExtension.POSITIONS;

public class MassClaimClaim extends SubCommand {

    private final ClaimManager manager;

    public MassClaimClaim(ClaimManager manager) {
        super("claim", "claim.mass.claim", "command.claim-mass.claim.description", false);
        this.manager = manager;
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        UUID uuid = player.getUniqueId();
        if (POSITIONS.containsKey(uuid)) {
            Position position = POSITIONS.get(uuid);

            if (position.getPos1() != null && position.getPos2() != null) {
                Faction faction = FactionUtility.getPlayerFaction(player);

                Cuboid cuboid = new Cuboid(position.getPos1(), position.getPos2());

                ArrayList<Chunk> claimedChunks = new ArrayList<>();

                for (Block block : cuboid) {
                    if (!claimedChunks.contains(block.getChunk())) {
                        manager.claimChunk(faction, block.getChunk());
                        claimedChunks.add(block.getChunk());
                    }
                }

                POSITIONS.remove(uuid);
                PositionWand.removePlayer(player);
                PositionWand.USER_ITEMS.remove(player.getUniqueId());
                Language.sendMessage("command.claim-mass.claim.success", player);
            } else {
                Language.sendMessage("command.claim-mass.claim.not-all-positions-set", player);
            }
        } else {
            Language.sendMessage("command.claim-mass.claim.not-all-positions-set", player);
        }
    }

    @Override
    protected List<String> CommandTab(Player arg0, String[] arg1) {
        return null;
    }

}
