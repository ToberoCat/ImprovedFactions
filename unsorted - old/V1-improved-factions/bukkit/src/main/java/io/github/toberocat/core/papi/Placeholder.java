package io.github.toberocat.core.papi;

import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import org.bukkit.OfflinePlayer;

public interface Placeholder {
    String call(OfflinePlayer player);
}
