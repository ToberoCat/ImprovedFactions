package io.github.toberocat.core.utility.factions.rank;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminRank extends Rank{
    public static final String registry = "Admin";
    public AdminRank() {
        super("Admin", registry, true);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage(LangMessage.RANK_ADMIN_DESCRIPTION, player);
    }

    @Override
    public ItemStack getItem() {
        return Utility.createItem(Material.GRASS_BLOCK, getDisplayName());
        //return Utils.getSkull("http://textures.minecraft.net/texture/9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f", 1, getDisplayName());
    }
}
