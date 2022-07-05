package io.github.toberocat.improvedfactions.ranks;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Rank {

    public static List<Rank> ranks = new ArrayList<>();

    private String displayName;
    private final String registryName;
    private boolean isAdmin;

    public Rank(String displayName, String registryName, boolean isAdmin) {
        this.displayName = displayName;
        this.registryName = registryName;
        this.isAdmin = isAdmin;
        ranks.add(this);
    }

    public static void Init() {
        new AdminRank();
        new MemberRank();
        new OwnerRank();
        new NewMemberRank();
        new GuestRank();
        new AllyRank();
    }

    public String[] getDescription() {
        int maxlines = 3;
        String[] lines = new String[maxlines+1];
        for (int i = 0; i < maxlines; i++) {
            String line = Language.format(description(i));
            if (!line.isEmpty()) {
                lines[i] = line;
            }
        }
        return lines;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRegistryName() {
        return registryName;
    }

    public abstract String description(int line);

    public ItemStack getItem() {
        return Utils.getSkull("http://textures.minecraft.net/texture/126b772329cf32f8643c4928626b6a325233ff61aa9c7725873a4bd66db3d692", 1, displayName);
    }

    @Override
    public String toString() {
        return registryName;
    }

    public static Rank fromString(String str) {
        for (Rank rank : ranks) {
            if (rank.toString().equals(str)) return rank;
        }
        return null;
    }
}
