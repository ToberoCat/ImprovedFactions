package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

public class OwnerRank extends Rank{
    public static final String registry = "Owner";
    public OwnerRank() {
        super("Owner", registry, true);
    }

    @Override
    public String description(int line) {
        if (line == 0) {
            return Language.format("&8Owners have rights");
        } else if (line == 1) {
            return Language.format("&8to delete the faction");
        }
        return "";
    }

    @Override
    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/67e29a58c060511ff0350241c9c86496947d8f84bf32f2df35ebe3742f0e2029", 1, getDisplayName());
    }
}
