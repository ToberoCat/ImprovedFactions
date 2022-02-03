package io.github.toberocat.core.utility.messages;

import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.dynamic.loaders.PlayerJoinLoader;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MessageSystem extends PlayerJoinLoader {

    private static HashMap<UUID, ArrayList<String>> MESSAGES;

    public MessageSystem() {
        Subscribe(this);
        MESSAGES = new HashMap<>();
    }

    public static void sendMessage(UUID uuid, String message) {
        AsyncCore.Run(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
           if (player.isOnline()) {
               player.getPlayer().sendMessage(message);
           } else {
               ArrayList<String> messages = MESSAGES.putIfAbsent(uuid, new ArrayList<>());
               messages.add(message);
           }
        });
    }

    @Override
    protected void loadPlayer(Player player) {
        AsyncCore.Run(() -> {
            UUID uuid = player.getUniqueId();
            if (MESSAGES.containsKey(uuid)) {
                for (String message : MESSAGES.get(uuid)) {
                    player.sendMessage(message);
                }
                MESSAGES.remove(uuid);
            } else if (DataAccess.exists("Messages", uuid.toString())) {
                ArrayList<String> messages = DataAccess.getFile("Messages", uuid.toString(), ArrayList.class);
                for (String message : MESSAGES.get(uuid)) {
                    player.sendMessage(message);
                }
            }

        });
    }

    @Override
    protected void unloadPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (MESSAGES.containsKey(uuid)) {
            DataAccess.addFile("Messages", uuid.toString(), MESSAGES.get(uuid));
            MESSAGES.remove(uuid);
        }
    }

    @Override
    protected void Disable() {
        super.Disable();
        for (UUID uuid : MESSAGES.keySet()) {
            DataAccess.addFile("Messages", uuid.toString(), MESSAGES.get(uuid));
        }
        MESSAGES.clear();
    }
}
