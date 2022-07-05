package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.entity.Player;

public interface Callback {
    void CallBack(Faction faction, Player player, Object object);
}
