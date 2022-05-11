package io.github.toberocat.core.factions.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.rank.GuestRank;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.factions.rank.members.*;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.events.faction.FactionUpdateMemberRankEvent;
import io.github.toberocat.core.utility.items.ItemCore;
import io.github.toberocat.core.utility.settings.FactionSettings;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FactionPerm {
    public static final Map<String, RankSetting> DEFAULT_RANKS = new HashMap<>();

    public static final String BREAK_PERM = "permission.break";
    public static final String PLACE_PERM = "permission.place";
    public static final String INTERACT_PERM = "permission.interact";

    private FactionSettings factionSettings;
    private Map<String, RankSetting> rankSetting;
    private Map<UUID, String> memberRanks;

    @JsonIgnore
    private Faction faction;

    public FactionPerm() {
    }

    public FactionPerm(Faction faction) {
        this.factionSettings = new FactionSettings();
        this.faction = faction;
        rankSetting = new HashMap<>(DEFAULT_RANKS);
        memberRanks = new HashMap<>();
        Setting.populateSettings(FactionSettings.DEFAULT_SETTINGS, factionSettings.getFactionSettings());

        for (String key : DEFAULT_RANKS.keySet()) {
            RankSetting defaultSettings = DEFAULT_RANKS.get(key);
            String[] selected = rankSetting.get(key).getSelected();
            rankSetting.replace(key, defaultSettings);
            rankSetting.get(key).setSelected(selected);

            rankSetting.get(key).setDisplay(defaultSettings.getDisplay());
        }
    }

    public static void register() {
        DEFAULT_RANKS.put(PLACE_PERM, new RankSetting(PLACE_PERM, new String[]{OwnerRank.registry, AdminRank.registry,
                ElderRank.registry, MemberRank.registry, ModeratorRank.registry
        }, ItemCore.create(PLACE_PERM, Material.BRICKS, "&ePlace permission")));

        DEFAULT_RANKS.put(BREAK_PERM, new RankSetting(BREAK_PERM, new String[]{OwnerRank.registry, AdminRank.registry,
                ElderRank.registry, MemberRank.registry, ModeratorRank.registry
        }, ItemCore.create(BREAK_PERM, Material.WOODEN_PICKAXE, "&eBreak permission")));

        DEFAULT_RANKS.put(INTERACT_PERM, new RankSetting(INTERACT_PERM, new String[]{OwnerRank.registry, AdminRank.registry,
                ElderRank.registry, MemberRank.registry, ModeratorRank.registry
        }, ItemCore.create(INTERACT_PERM, Material.CHEST, "&eInteract permission")));
    }

    public Rank getPlayerRank(Player player) {
        if (!faction.isMember(player)) return Rank.fromString(GuestRank.register);

        String playerFaction = FactionUtility.getPlayerFactionRegistry(player);
        if (faction.getRelationManager().getAllies().contains(playerFaction))
            return FactionUtility.getFactionByRegistry(playerFaction).getPlayerRank(player);
        return Rank.fromString(memberRanks.get(player.getUniqueId()));
    }

    public void setRank(OfflinePlayer player, String rank) {
        memberRanks.put(player.getUniqueId(), rank);
        Utility.callEvent(new FactionUpdateMemberRankEvent(faction, player, rank));
    }

    public Map<String, Setting> getFactionSettings() {
        return factionSettings.getFactionSettings();
    }

    public void setFactionSettings(Map<String, Setting> factionSettings) {
        if (this.factionSettings == null) this.factionSettings = new FactionSettings();
        this.factionSettings.setFactionSettings(factionSettings);
        Setting.populateSettings(FactionSettings.DEFAULT_SETTINGS, factionSettings);
    }

    public Map<String, RankSetting> getRankSetting() {
        return rankSetting;
    }

    public void setRankSetting(Map<String, RankSetting> rankSetting) {
        this.rankSetting = rankSetting;
    }

    public Map<UUID, String> getMemberRanks() {
        return memberRanks;
    }

    public void setMemberRanks(Map<UUID, String> memberRanks) {
        this.memberRanks = memberRanks;
    }

    @JsonIgnore
    public Faction getFaction() {
        return faction;
    }

    @JsonIgnore
    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
