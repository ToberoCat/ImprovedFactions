package io.github.toberocat.core.commands.factions.claim;

import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.command.auto.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.command.auto.WorldAutoSubCommand;
import io.github.toberocat.core.utility.exceptions.chunks.ChunkAlreadyClaimedException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClaimSubCommand extends WorldAutoSubCommand {
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
    public List<String> suggestedRadius(@NotNull Player player, @NotNull String[] args) {
        return List.of("1", "5", "10");
    }

    @Override
    public void single(@NotNull Player player, @NotNull Chunk action) {
        String registry = FactionHandler.getPlayerFaction(player);
        if (registry == null) return;

        try {
            FactionHandler.getFaction(registry).getClaims().claim(action);

            Language.sendMessage("command.faction.claim.success", player);
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        } catch (ChunkAlreadyClaimedException e) {
            Parser.run("command.faction.claim.chunk-already-claimed")
                    .parse("{claimed}", e.getRegistry())
                    .send(player);
        }
    }
}
