package io.github.toberocat.core.utility.history;

import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.history.territory.Territory;
import io.github.toberocat.core.utility.history.territory.TerritorySwitch;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class History {

    public static void logTerritorySwitch(Player player, Territory from, Territory to) {
        AsyncCore.Run(() -> {
            HashMap<String, TerritorySwitch> data;
            if (DataAccess.exists("History/Territory", player.getUniqueId().toString())) {
                data = DataAccess.getFile("History/Territory", player.getUniqueId().toString(), HashMap.class);
            } else {
                data = new HashMap<>();
            }

            DataAccess.addFile("History/Territory", player.getUniqueId().toString(), data);
        });
    }
}
