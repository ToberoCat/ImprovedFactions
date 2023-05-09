package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class FactionRankOption implements PlayerOption {
    private final @NotNull String rankId;

    public FactionRankOption(@NotNull String rankId) {
        this.rankId = rankId;
    }

    @Override
    public void canExecutePlayer(@NotNull FactionPlayer player,
                                 @NotNull String[] args) throws CommandException {
        try {
            Rank rank = player.getRank();
            if (!checkRank(player))
                throw new CommandException("exceptions.wrong-rank", () -> new PlaceholderBuilder()
                        .placeholder("player", player)
                        .placeholder("needed-rank", Rank.fromString(rankId))
                        .placeholder("player-rank", rank)
                        .getPlaceholders());
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            throw new CommandException("exceptions.need-faction", () -> new PlaceholderBuilder()
                    .placeholder("player", player)
                    .getPlaceholders());
        }
    }

    @Override
    public boolean show(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof FactionPlayer player))
            return false;
        try {
            return checkRank(player);
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            return false;
        }
    }

    private boolean checkRank(@NotNull FactionPlayer player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return player.getRank().getRegistry().equals(rankId);
    }
}
