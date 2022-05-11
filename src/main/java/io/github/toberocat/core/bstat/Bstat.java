package io.github.toberocat.core.bstat;

import io.github.toberocat.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public class Bstat {
    public static void register(MainIF plugin) {
        try {
            Metrics metrics = new Metrics(plugin, 14810);

            metrics.addCustomChart(new Metrics.AdvancedPie("used_languages", () -> {
                Map<String, Integer> valueMap = new HashMap<>();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    String locale = player.getLocale();
                    if (!valueMap.containsKey(locale)) valueMap.put(locale, 0);

                    int value = valueMap.get(locale);
                    valueMap.put(locale, value + 1);
                }

                return valueMap;
            }));

            MainIF.logMessage(Level.INFO, "&aSuccessfully loaded &6bstat &adata collection");
        } catch (Exception e) {
            MainIF.logMessage(Level.INFO, "&cCouldn't load bstat");
        }
    }
}
