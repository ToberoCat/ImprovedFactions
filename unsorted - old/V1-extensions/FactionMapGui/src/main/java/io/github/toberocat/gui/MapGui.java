package io.github.toberocat.gui;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.gui.AbstractGui;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static java.lang.Math.ceil;

public class MapGui extends AbstractGui {
    public MapGui(Player player) {
        super(player, createInv(player));

        render(player);
    }

    private static Inventory createInv(Player player) {
        return Bukkit.createInventory(player, 54, Language.getMessage("gui.claim-gui.title", player));
    }

    private void render(Player player) {
        Chunk center = player.getLocation().getChunk();
        int dstH = 5;
        int dstW = 9;

        int leftCornerX = (int) (center.getX() - ceil(dstW / 2f));
        int leftCornerZ = (int) (center.getZ() - ceil(dstH / 2f));

        World world = player.getLocation().getWorld();
        if (world == null) return;

        for (int x = 0; x < dstW; x++)
            for (int z = 0; z < dstH; z++) {
                addItem(x, z, world.getChunkAt(leftCornerX + x, leftCornerZ + z), player);
            }
    }

    private Material getZoneMaterial(@NotNull String zone) {
        return switch (zone) {
            case ClaimManager.SAFEZONE_REGISTRY -> Material.LIGHT_BLUE_STAINED_GLASS_PANE;
            case ClaimManager.WARZONE_REGISTRY -> Material.ORANGE_STAINED_GLASS_PANE;
            case ClaimManager.UNCLAIMABLE_REGISTRY -> Material.GRAY_STAINED_GLASS_PANE;
            default -> Material.GREEN_STAINED_GLASS_PANE;
        };
    }

    private void addItem(int x, int z, Chunk chunk, Player player) {
        if (chunk == null) return;

        String factionRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(chunk);
        Material symbol = Material.GREEN_STAINED_GLASS_PANE;
        String title = "§2Wildness";
        if (factionRegistry != null) {
            if (ClaimManager.isManageableZone(factionRegistry)) { // Zone
                symbol = getZoneMaterial(factionRegistry);
                title = ClaimManager.getDisplay(factionRegistry);
            } else { // Faction
                String playerRegistry = FactionUtility.getPlayerFactionRegistry(player);
                if (playerRegistry == null) return;

                symbol = factionRegistry.equals(playerRegistry) ?
                        Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;

                Faction registryFaction = FactionUtility.getFactionByRegistry(factionRegistry);
                if (registryFaction == null) return;

                title = (factionRegistry.equals(playerRegistry) ? "§a" : "§c") + registryFaction.getDisplayName();
            }
        }

        if (player.getLocation().getChunk() == chunk) {
            symbol = Material.YELLOW_STAINED_GLASS_PANE;
            title += " (You)";
        }

        addSlot(Utility.createItem(symbol, title), 0, z * 9 + x, (user) -> {
            MainIF.getIF().getClaimManager().claimChunk(FactionUtility.getPlayerFaction(user), chunk);
            render(user);
        });
    }

    @Override
    protected void addPage() {
        pages.add(new Page(inventory.getSize()));
    }

    @Override
    protected GuiSettings readSettings() {
        return null;
    }
}
