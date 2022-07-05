package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

public class MemberRank extends Rank {
    public static final String registry = "Member";
    public MemberRank() {
        super("Member", registry, false);
    }

    @Override
    public String description(int line) {
        if (line == 0) {
            return Language.format("&8Members are people");
        } else if (line == 1) {
            return Language.format("&8who have joined your faction");
        }
        return "";
    }

    @Override
    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/54bf893fc6defad218f7836efefbe636f1c2cc1bb650c82fccd99f2c1ee6", 1, getDisplayName());
    }
}
