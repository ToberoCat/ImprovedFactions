package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactionRankPermission {

    private List<Rank> ranks;
    private ItemStack itemStack;

    public FactionRankPermission(ItemStack itemStack, Rank[] ranks) {
        this.ranks = new ArrayList<>(Arrays.asList(ranks));
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return "FactionRankPermission{" +
                "ranks=" + ranks +
                '}';
    }

    public static FactionRankPermission fromString(String string) {
        string = string.replace("FactionRankPermission{", "");
        string = string.replace("}", "");

        String key = string.split("::")[0];
        string = string.split("::")[1];
        Rank[] ranks = null;

        String[] parms = string.split("[=]");
        for (int i = 0;  i < parms.length; i++) {
            String parm = parms[i];
            if (parm.equals("ranks")) {
                String[] items = parms[i + 1].split(", ");
                List<Rank> _ranks = new ArrayList<>();
                for (String item : items) {
                    String value = item.replace("[", "").replace("]", "");
                    _ranks.add(Rank.fromString(value));
                }
                ranks = new Rank[_ranks.size()];
                ranks = _ranks.toArray(ranks);
            }
        }
        FactionRankPermission perm = getPermisson(key);
        perm.ranks = new ArrayList<>(Arrays.asList(ranks));
        return perm;
    }

    private static FactionRankPermission getPermisson(String key) {
        return FactionSettings.RANKS.get(key);
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }
}
