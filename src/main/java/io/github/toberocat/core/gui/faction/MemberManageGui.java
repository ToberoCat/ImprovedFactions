package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.gui.page.Page;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class MemberManageGui extends Gui {
    private final Player self;
    private final OfflinePlayer managed;
    private final Faction faction;
    private String selectedRank;
    private final EnumSetting rankSelector;
    private final String[] ranks;
    private final Rank selfRank;
    private final Rank managedRank;

    public MemberManageGui(Player player, OfflinePlayer managedPlayer, Faction faction, GUISettings memberGUISettings) {
        super(player, createInventory(player, managedPlayer), new GUISettings());

        settings.setQuitIcon(true);
        settings.setQuitCallback(() -> goBack(player, faction, memberGUISettings));

        this.faction = faction;
        this.managed = managedPlayer;
        this.self = player;

        this.selfRank = faction.getPlayerRank(self);
        this.managedRank = faction.getPlayerRank(managed);

        ranks = getManageableRanks(selfRank);
        rankSelector = new EnumSetting(ranks, "gui.rank_selector",
                managedRank.getItem(self));
        int selected = ArrayUtils.indexOf(ranks, managedRank.getRegistryName());
        if (selected != -1) rankSelector.setSelected(selected);

        render(memberGUISettings);
    }

    private static void goBack(Player player, Faction faction, GUISettings settings) {
        new MemberGui(player, faction, settings);
    }

    private static Inventory createInventory(Player player, OfflinePlayer managedPlayer) {
        return Bukkit.createInventory(player, 36, "§eManage §e§l" + managedPlayer.getName());
    }

    private String[] getManageableRanks(Rank rank) {
        return Rank.getPriorityRanks(rank)
                .map(Rank::getRegistryName)
                .toArray(String[]::new);
    }

    private void render(GUISettings backSettings) {
        rankSelector.setDisplay(managedRank.getItem(self));
        if (self.getUniqueId().equals(managed.getUniqueId())) {
            addSlot(Utility.createItem(Material.WRITTEN_BOOK, "&eCan't manage yourself", new String[]{"&8You aren't able to",
                    "&8manage yourself", "&8Go back and manage some else"}), 0, 13, () -> {
            });
            return;
        }
        addSlot(Utility.getSkull(managed, 1, "§eKick " + managed.getName(), new String[]{
                "§8Click to kick", "§6§lWarning: §7This will remove the player from your faction",
                "§7Can't be undone"
        }), 0, 11, () -> {
            Result result = faction.kick(managed);
            Language.sendRawMessage(result.getPlayerMessage(), self);
            goBack(self, faction, backSettings);
        });

        if (faction.hasPermission(self, FactionPerm.MANAGE_RANKS_PERM)
                && managedRank.getRawPriority() < Rank.getPriority(selfRank)) {
            addSlot(Setting.getSlot(rankSelector, self, () -> {
                slots.remove(currentPage);
                slots.add(currentPage, new Page());
                selectedRank = ranks[rankSelector.getSelected()];
                render(backSettings);
            }), 0, 13);
        }

        addSlot(Utility.getSkull(managed, 1, "§eBan " + managed.getName(), new String[]{
                "§8Click to ban", "§6§lWarning: §7This will remove the player from your faction",
                "§7 and never lets them join again. Can't be undone"
        }), 0, 15, () -> {
            Result result = faction.ban(managed);
            Language.sendRawMessage(result.getPlayerMessage(), self);
            goBack(self, faction, backSettings);
        });
    }

    @Override
    protected void close() {
        if (selectedRank == null) return;

        faction.getFactionPerm().setRank(managed, selectedRank);
    }
}
