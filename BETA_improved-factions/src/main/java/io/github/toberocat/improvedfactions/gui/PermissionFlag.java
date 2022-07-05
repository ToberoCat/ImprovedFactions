package io.github.toberocat.improvedfactions.gui;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionRankPermission;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PermissionFlag {

    private final String flagName;
    private final Material material;

    private FactionRankPermission permissions;

    public PermissionFlag(String flagName, Material material, FactionRankPermission permissions) {
        this.flagName = Language.format(flagName);
        this.material = material;
        this.permissions = permissions;
    }

    public void openInventory(Player p, Faction faction) {
        new FactionRankEditor(p, flagName, faction, permissions);
    }

    public ItemStack getItem() {
        return Utils.createItem(material, flagName ,
                new String[]{Language.format("&8Click to edit")});
    }
}
