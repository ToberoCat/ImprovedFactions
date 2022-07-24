package io.github.toberocat.core.commands.factions.claim;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.auto.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

public class ClaimSubCommand extends AutoSubCommand {
    public ClaimSubCommand() {
        super("claim", "command.faction.claim.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    public String getEnabledKey() {
        return "command.faction.claim.auto.enable";
    }

    @Override
    public String getDisabledKey() {
        return "command.faction.claim.auto.disable";
    }

    @Override
    public void onSingle(Player player) {
        String registry = FactionHandler.getPlayerFaction(player);
        if (registry == null) return;

        try {
            FactionHandler.getFaction(registry).getClaims().claim(player.getLocation().getChunk());

            Language.sendMessage("command.faction.claim.success", player)
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        } catch (ChunkAlreadyClaimedException e) {
            e.printStackTrace();
        }

        if (result.isSuccess()) Language.sendMessage("command.faction.claim.success", player);
        else
            Language.sendMessage("command.faction.claim.failed", player,
                    new Parseable("{error}", result.getPlayerMessage()));
    }
}
