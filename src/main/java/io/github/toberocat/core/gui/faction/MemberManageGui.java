package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.factions.rank.members.OwnerRank;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
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

public class MemberManageGui extends Gui {
    private final Player self;
    private final OfflinePlayer managed;
    private final Faction faction;
    private final EnumSetting rankSelector;
    private final String[] ranks;
    private final Rank selfRank;
    private final Rank managedRank;
    private String selectedRank;

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
        if (selectedRank != null) {
            Rank rank = Rank.fromString(selectedRank);
            if (rank != null) rankSelector.setDisplay(rank.getItem(self));
        }
        if (self.getUniqueId().equals(managed.getUniqueId())) {
            addSlot(Utility.createItem(Material.WRITTEN_BOOK, "&eCan't manage yourself", new String[]{"&8You aren't able to",
                    "&8manage yourself", "&8Go back and manage some else"}), 0, 13, () -> {
            });
            return;
        }

        if (Rank.getPriority(managedRank) > Rank.getPriority(selfRank)) {
            addSlot(Utility.createItem(Material.WRITTEN_BOOK, "&eCan't manage this user", new String[]{"&8You aren't able to",
                    "&8manage someone with a higher", "&8rank than you. Go back"}), 0, 13, () -> {
            });
            return;
        }

        if (faction.hasPermission(self, FactionPerm.KICK_PERM)) {
            addSlot(Utility.getSkull(managed, 1, "§eKick " + managed.getName(), new String[]{
                    "§8Click to kick", "§6§lWarning: §7This will remove the player from your faction",
                    "§7Can't be undone"
            }), 0, 11, () -> {
                Result result = faction.kick(managed);
                Language.sendRawMessage(result.getPlayerMessage(), self);
                goBack(self, faction, backSettings);
            });
        } else {
            addSlot(Utility.createItem(Material.BARRIER, "§cKick " + managed.getName(), new String[]{
                    "§8You don't have enough", "§8permission to kick someone"
            }), 0, 11, () -> {
            });
        }

        if (faction.hasPermission(self, FactionPerm.MANAGE_RANKS_PERM)) {
            addSlot(Setting.getSlot(rankSelector, self, () -> {
                slots.remove(currentPage);
                slots.add(currentPage, new Page());
                selectedRank = ranks[rankSelector.getSelected()];
                render(backSettings);
            }), 0, 13);
        } else {
            addSlot(managedRank.getItem(self), 0, 13, () -> {
            });
        }

        if (faction.hasPermission(self, FactionPerm.BAN_PERM)) {
            addSlot(Utility.getSkull(managed, 1, "§eBan " + managed.getName(), new String[]{
                    "§8Click to ban", "§6§lWarning: §7This will remove the player from your faction",
                    "§7 and never lets them join again. Can't be undone"
            }), 0, 15, () -> {
                Result result = faction.ban(managed);
                Language.sendRawMessage(result.getPlayerMessage(), self);
                goBack(self, faction, backSettings);
            });
        } else {
            addSlot(Utility.createItem(Material.BARRIER, "§cBan " + managed.getName(), new String[]{
                    "§8You don't have enough", "§8permission to ban someone"
            }), 0, 15, () -> {
            });
        }

        if (selfRank.getRegistryName().equals(OwnerRank.registry)) {
            addSlot(Utility.createItem(Material.ENDER_EYE, "§cTransfer ownership", new String[]{
                    "§6&lWarning: §7This will take the",
                    "§7owner rights and give it to " + managed.getName(),
                    "",
                    "§cCan't be undone"
            }), 0, 22, () -> AsyncTask.runLaterSync(0, () -> {
                faction.transferOwnership(self, managed);
                self.closeInventory();
            }));
        }
    }

    @Override
    protected void close() {
        if (selectedRank == null) return;

        faction.getFactionPerm().setRank(managed, selectedRank);
    }
}
