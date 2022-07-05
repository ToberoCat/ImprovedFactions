package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

public class GuestRank extends Rank {
    public static final String registry = "guest";
    public GuestRank() {
        super("Guest rank", registry, false);
    }

    @Override
    public String description(int line) {
        if (line == 0) {
            return Language.format("&8They are everyone");
        } else if (line == 1) {
            return Language.format("&8whom is not in your faction / ally");
        }
        return "";
    }

    @Override
    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/6d8000d5419860ff3de53ba559b9ece79fb1b648e099f023ba48084cdcef8b0c", 1, getDisplayName());
    }
}
