package io.github.toberocat.core.utility.map;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.dynamic.loaders.PlayerJoinLoader;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class MapHandler extends PlayerJoinLoader {

    private final Map<UUID, Integer> TASK_ID = new HashMap<>();

    public static void register() {
        Subscribe(new MapHandler());
    }

    public static void startMap(Player player) {
    }

    public static void getSingleMap(Player player) {
        ItemStack map = Utility.createItem(Material.MAP, "&eFaction Map", new String[]{
                "&8This is a custom rendered map", "&8It will display claimed chunks around you", "",
                "&e&lMap: &8" + player.getName()
        });

        MapMeta itemMeta = (MapMeta) map.getItemMeta();
        itemMeta.getMapView().addRenderer(new MapRenderer() {
            @Override
            public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
                // mapCanvas.drawImage();
            }
        });

        if (player.getInventory().getItemInOffHand().getType() == Material.AIR) {
            player.getInventory().setItemInOffHand(map);
        } else {
            player.getInventory().addItem(map);
        }
    }

    public static void closeMap(Player player) {
    }

    @Override
    protected void loadPlayer(Player value) {
    }

    @Override
    protected void unloadPlayer(Player player) {
        if (TASK_ID.containsKey(player.getUniqueId())) {
            AtomicReference<ItemStack> map = new AtomicReference<>();
            player.getInventory().forEach(item -> {
                if (map.get() != null) return;
                List<String> lore = Utility.getLore(item);
                if (lore.size() < 1) return;
                String mapId = lore.get(lore.size() - 1);

                if (ChatColor.stripColor(mapId).equals("Map: " + player.getName())) {
                    map.set(item);
                }
            });

            if (map.get() == null) return;
            player.getInventory().remove(map.get());
            TASK_ID.remove(player.getUniqueId());
        }
    }

    @Override
    protected void Disable() {
        super.Disable();
    }
}
