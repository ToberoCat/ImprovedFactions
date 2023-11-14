package io.github.toberocat.improvedfactions.utils.options;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class FactionPriorityOption implements PlayerOption {
    private final int minPriority;

    public FactionPriorityOption(int minPriority) {
        this.minPriority = minPriority;
    }

    @Override
    public @NotNull String[] executePlayer(@NotNull FactionPlayer player,
                              @NotNull String[] args) throws CommandException {
        try {
            Rank rank = player.getRank();
            if (!checkRank(player))
                throw new CommandException("exceptions.missing-priority", () -> new PlaceholderBuilder()
                        .placeholder("player", player)
                        .placeholder("min-priority", minPriority)
                        .placeholder("player-priority", Rank.getPriority(rank))
                        .getPlaceholders());
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            throw new CommandException(e);
        }
        return args;
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
        return Rank.getPriority(player.getRank()) >= minPriority;
    }
}
