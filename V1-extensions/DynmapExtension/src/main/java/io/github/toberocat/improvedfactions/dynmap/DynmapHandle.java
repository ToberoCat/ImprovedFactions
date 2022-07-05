package io.github.toberocat.improvedfactions.dynmap;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.improvedfactions.DynmapExtension;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.logging.Level;

public class DynmapHandle {

    private final String setId = "faction_set";


    private final DynmapAPI dynmap;
    private final MarkerAPI markerAPI;
    private final MarkerSet set;
    private final DynmapExtension extension;

    public DynmapHandle(DynmapExtension extension, DynmapAPI dynmap) {
        this.dynmap = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        this.markerAPI = dynmap.getMarkerAPI();
        this.extension = extension;

        MarkerSet nullSet = markerAPI.getMarkerSet(setId);
        if (nullSet == null) nullSet = markerAPI.createMarkerSet(setId, "Faction claims", null, false);

        this.set = nullSet;
    }

    public void claim(Chunk chunk, int color, String title, String... description) {
        double[] d1 = {chunk.getBlock(0, 0, 0).getLocation().getX(),
                chunk.getBlock(15, 0, 15).getLocation().getX() + 1};
        double[] d2 = {chunk.getBlock(0, 0, 0).getLocation().getZ(),
                chunk.getBlock(15, 0, 15).getLocation().getZ() + 1};

        String id = "factions-" + chunk.getX() + "-" + chunk.getZ();
        set.createAreaMarker("factions-" + chunk.getX() + "-" + chunk.getZ(), title, false,
                chunk.getWorld().getName(), d1, d2, false);
        AreaMarker marker = set.findAreaMarker(id);

        if (marker == null) {
            extension.log(Level.WARNING, "&eDynamp couldn't load chunk &6" + chunk.getX() + "&e, &6"
                    + chunk.getZ() + "&e in world &6" + chunk.getWorld());
            return;
        }


        marker.setFillStyle(extension.opacity, color);
        marker.setLineStyle(color, extension.opacity, marker.getLineWeight());

        StringBuilder builder = new StringBuilder()
                .append("<div>")
                .append(String.format("<h1>%s</h1>", title));

        for (String desc : description) builder.append(String.format("<h2>%s</h2>", desc));

        builder.append("</div>");

        marker.setDescription(builder.toString());
    }

    public void claim(Chunk chunk, Faction faction) {
        claim(chunk, faction.getColor(), faction.getDisplayName(), "Registry: " + faction.getRegistryName(),
                "Description: " + String.join("\n", faction.getDescription()),
                "Motd: " + faction.getMotd(),
                "Owner: " + Bukkit.getOfflinePlayer(faction.getOwner()).getName());

        /*
        marker.setFillStyle(marker.getFillOpacity(),
                FactionColors.values()[faction
                .getFactionPerm()
                .getFactionSettings()
                .get("universal_color")
                .getSelected()].);
         */

    }

    public void unclaim(Chunk chunk) {
        AreaMarker marker = set.findAreaMarker("factions-" + chunk.getX() + "-" + chunk.getZ());
        if (marker == null) return;

        marker.deleteMarker();
    }
}
