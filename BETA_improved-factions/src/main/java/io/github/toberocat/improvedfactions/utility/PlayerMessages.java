package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerMessages {

    private Map<UUID, List<String>> messages = new HashMap<>();

    public PlayerMessages(ImprovedFactionsMain plugin) {
        LoadPlayerMessages(plugin);
    }

    private void LoadPlayerMessages(ImprovedFactionsMain plugin) {
        if (plugin.getMessagesData().getConfig().getConfigurationSection("messages") == null) return;
        for (String key : plugin.getMessagesData().getConfig().getConfigurationSection("messages").getKeys(false)) {
            List<String> _messages = plugin.getMessagesData().getConfig().getStringList("messages." + key);
            messages.put(UUID.fromString(key), _messages);
        }
    }

    public void SavePlayerMessages() {
        ImprovedFactionsMain.getPlugin().getMessagesData().getConfig().set("messages", null);
        for (UUID uuid : messages.keySet()) {
            ImprovedFactionsMain.getPlugin().getMessagesData().getConfig().set("messages." + uuid.toString(), messages.get(uuid));
        }
        ImprovedFactionsMain.getPlugin().getMessagesData().saveConfig();
    }

    public void SendMessage(OfflinePlayer offlinePlayer, String message) {
        Player player = Bukkit.getPlayer(offlinePlayer.getUniqueId());
        if (player != null && player.isOnline()) {
            player.sendMessage(message);
        } else {
            if (messages.containsKey(offlinePlayer.getUniqueId())) {
                messages.get(offlinePlayer.getUniqueId()).add(Language.format(message));
            } else {
                messages.put(offlinePlayer.getUniqueId(), Collections.singletonList(message));
            }
        }
    }

    public void ReceiveMessages(Player player) {
        if (messages.containsKey(player.getUniqueId())) {
            for (String message : messages.get(player.getUniqueId())) {
                player.sendMessage(message);
            }
            messages.remove(player.getUniqueId());
        }
    }
}
