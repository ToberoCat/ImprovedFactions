package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

public class AllyRank extends Rank{
    public static final String registry = "allyrank";
    public AllyRank() {
        super("Ally rank", registry, false);
    }

    @Override
    public String description(int line) {
        if (line == 0) {
            return Language.format("&8Allies are your");
        } else if (line == 1) {
            return Language.format("&8factions friends");
        }
        return "";
    }

    @Override
    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/72bd318d67bc8c92e468a21000265a349d6043e494a19e4ef14060319e8bd834", 1, getDisplayName());
    }
}
