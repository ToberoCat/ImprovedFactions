package io.github.toberocat.core.utility.messages;

import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.dynamic.loaders.PlayerJoinLoader;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

// ToDo: Make message system useful
public class MessageSystem extends PlayerJoinLoader {

    private static HashMap<UUID, ArrayList<String>> MESSAGES;

    public MessageSystem() {
        Subscribe(this);
        MESSAGES = new HashMap<>();
    }

    public static void sendMessage(UUID uuid, String message) {
        /*
        AsyncTask.run(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if (player.isOnline()) {
                player.getPlayer().sendMessage(message);
            } else {
                ArrayList<String> messages = MESSAGES.putIfAbsent(uuid, new ArrayList<>());
                messages.add(message);
            }
        });
         */
    }

    @Override
    protected void loadPlayer(Player player) {
        AsyncTask.run(() -> {
            UUID uuid = player.getUniqueId();
            if (MESSAGES.containsKey(uuid)) {
                for (String message : MESSAGES.get(uuid)) {
                    player.sendMessage(message);
                }
                MESSAGES.remove(uuid);
            } else if (FileAccess.existsFolder("Messages", uuid.toString())) {
                //ArrayList<String> messages = DataAccess.get("Messages", uuid.toString(), ArrayList.class);
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
            //DataAccess.write("Messages", uuid.toString(), MESSAGES.get(uuid));
            MESSAGES.remove(uuid);
        }
    }

    @Override
    protected void Disable() {
        super.Disable();
        for (UUID uuid : MESSAGES.keySet()) {
            //DataAccess.write("Messages", uuid.toString(), MESSAGES.get(uuid));
        }
        MESSAGES.clear();
    }
}
