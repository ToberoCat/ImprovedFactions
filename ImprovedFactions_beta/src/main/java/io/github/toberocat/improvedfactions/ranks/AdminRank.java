package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

public class AdminRank extends Rank{
    public static final String registry = "Admin";
    public AdminRank() {
        super("Admin", registry, true);
    }

    @Override
    public String description(int line) {
        if (line == 0) {
            return Language.format("&8Admins have rights");
        } else if (line == 1) {
            return Language.format("&8to delete the faction");
        }
        return "";
    }

    @Override
    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/9631597dce4e4051e8d5a543641966ab54fbf25a0ed6047f11e6140d88bf48f", 1, getDisplayName());
    }
}
