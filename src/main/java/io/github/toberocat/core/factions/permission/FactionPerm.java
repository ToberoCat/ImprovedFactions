package io.github.toberocat.core.factions.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.rank.GuestRank;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.factions.rank.allies.*;
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
    public static final String MOUNT_PERM = "permission.mount";
    public static final String FACTION_SETTING_PERM = "permission.settings";
    public static final String MANAGE_RANKS_PERM = "permission.manage-rank";
    public static final String KICK_PERM= "permission.kick";
    public static final String BAN_PERM = "permission.ban";


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

        DEFAULT_RANKS.put(MOUNT_PERM, new RankSetting(MOUNT_PERM, new String[]{
                OwnerRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry,
                ModeratorRank.registry, AllyAdminRank.registry,
                AllyElderRank.registry, AllyMemberRank.registry, AllyOwnerRank.registry, AllyModeratorRank.registry,
                GuestRank.register
        }, ItemCore.create(MOUNT_PERM, Material.SADDLE, "&eMount permission", "&8Allows users to ride a lore, boat, horse, etc")));

        DEFAULT_RANKS.put(FACTION_SETTING_PERM, new RankSetting(FACTION_SETTING_PERM, new String[]{
                OwnerRank.registry, ModeratorRank.registry, AdminRank.registry
        }, ItemCore.create(FACTION_SETTING_PERM, Material.NETHER_STAR,
                "&eSetting permission", "&8Allows user to change ", "&8faction settings")));

        DEFAULT_RANKS.put(MANAGE_RANKS_PERM, new RankSetting(MANAGE_RANKS_PERM, new String[]{
                OwnerRank.registry, ModeratorRank.registry, AdminRank.registry
        }, ItemCore.create(MANAGE_RANKS_PERM, Material.BEACON, "&eManage ranks",
                "&8Allows user to modify ranks")));

        DEFAULT_RANKS.put(KICK_PERM, new RankSetting(KICK_PERM, new String[]{
                OwnerRank.registry, ModeratorRank.registry, AdminRank.registry
        }, ItemCore.create(KICK_PERM, Material.YELLOW_DYE, "&eKick members",
                "&8Kick a member for misbehaving")));

        DEFAULT_RANKS.put(BAN_PERM, new RankSetting(BAN_PERM, new String[]{
                OwnerRank.registry, AdminRank.registry
        }, ItemCore.create(BAN_PERM, Material.RED_DYE, "&eBan members",
                "&8Ban members for there lifetime - and unban them")));
    }

    public static void registerPermission(RankSetting setting) {
        DEFAULT_RANKS.put(setting.getSettingName(), setting);
    }

    public Rank getPlayerRank(OfflinePlayer player) {
        if (faction.isMember(player)) return Rank.fromString(memberRanks.get(player.getUniqueId()));

        Player on = player.getPlayer();
        if (on == null) return Rank.fromString(GuestRank.register);

        String playerFaction = FactionUtility.getPlayerFactionRegistry(on);
        if (faction.getRelationManager().getAllies().contains(playerFaction))
            return FactionUtility.getFactionByRegistry(playerFaction).getPlayerRank(player);

        return Rank.fromString(GuestRank.register);
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
        for (String key : DEFAULT_RANKS.keySet()) {
            RankSetting defaultSettings = DEFAULT_RANKS.get(key);

            if (!rankSetting.containsKey(key)) rankSetting.put(key, DEFAULT_RANKS.get(key));

            String[] selected = rankSetting.get(key).getSelected();
            rankSetting.replace(key, defaultSettings);
            rankSetting.get(key).setSelected(selected);

            rankSetting.get(key).setDisplay(defaultSettings.getDisplay());
        }
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
