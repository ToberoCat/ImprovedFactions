package io.github.toberocat.core.listeners;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.player.PlayerSettings;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import io.github.toberocat.core.utility.tips.TipOfTheDay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangeWorld implements Listener {
    @EventHandler
    public void change(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if (!TipOfTheDay.receivedTip(player) && !Utility.isDisabled(player.getWorld())) {
            Setting setting = PlayerSettings.getSettings(player.getUniqueId()).getSetting("showTips");
            if (setting instanceof BoolSetting bool) if (!bool.getSelected()) return;

            TipOfTheDay.sendTipOfTheDay(player);
        }
    }
}
