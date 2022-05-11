package io.github.toberocat.core.gui.faction;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MemberManageGui extends Gui {
    public MemberManageGui(Player player, OfflinePlayer managedPlayer, Faction faction, GUISettings memberGUISettings) {
        super(player, createInventory(player, managedPlayer), new GUISettings());

        settings.setQuitIcon(true);
        settings.setQuitCallback(() -> {
            System.out.println("Click");
            goBack(player, faction, memberGUISettings);
        });

        if (player.getUniqueId() != managedPlayer.getUniqueId()) {
            addSlot(Utility.getSkull(managedPlayer, 1, "§eKick " + managedPlayer.getName(), new String[]{
                    "§8Click to kick", "§6§lWarning: §7This will remove the player from your faction",
                    "§7Can't be undone"
            }), 0, 11, () -> {
                Result result = faction.kick(managedPlayer);
                Language.sendRawMessage(result.getPlayerMessage(), player);
                goBack(player, faction, memberGUISettings);
            });

            addSlot(Utility.createItem(Material.CARROT, "&eSwap rank", new String[] {
                    "§8Click to change player rank"
            }), 0, 13, () -> {
                AsyncTask.runLaterSync(0, () -> new ManagePlayerRankGui(player, managedPlayer, faction, memberGUISettings));
            });

            addSlot(Utility.getSkull(managedPlayer, 1, "§eBan " + managedPlayer.getName(), new String[]{
                    "§8Click to ban", "§6§lWarning: §7This will remove the player from your faction",
                    "§7 and never lets them join again. Can't be undone"
            }), 0, 15, () -> {
                Result result = faction.ban(managedPlayer);
                Language.sendRawMessage(result.getPlayerMessage(), player);
                goBack(player, faction, memberGUISettings);
            });
        } else {
            addSlot(Utility.createItem(Material.WRITTEN_BOOK, "&eCan't manage yourself", new String[]{"&8You aren't able to",
                    "&8manage yourself", "&8Go back and manage some else"}), 0, 13, () -> {
            });
        }
    }

    private static void goBack(Player player, Faction faction, GUISettings settings) {
        new MemberGui(player, faction, settings);
    }

    private static Inventory createInventory(Player player, OfflinePlayer managedPlayer) {
        return Bukkit.createInventory(player, 36, "§eManage §e§l" + managedPlayer.getName());
    }
}
