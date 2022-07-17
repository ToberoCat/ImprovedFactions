package io.github.toberocat.core.player;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.local.FactionUtility;
import io.github.toberocat.core.gui.faction.FactionSettingsGui;
import io.github.toberocat.core.gui.faction.MemberGui;
import io.github.toberocat.core.gui.faction.OnlineGUI;
import io.github.toberocat.core.gui.faction.RankGui;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.gui.settings.GuiSettings;
import io.github.toberocat.core.utility.language.Language;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SettingActions {

    public void renameFaction(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        new AnvilGUI.Builder().onClose((user) -> {
                })
                .onComplete((user, text) -> {
                    String rawText = ChatColor.stripColor(Language.format(text));
                    if (rawText.equalsIgnoreCase("safezone") ||
                            rawText.equalsIgnoreCase("warzone")) {
                        Language.sendRawMessage("Can't use a system name as faction name. Please choose another one", player);
                    } else {
                        faction.setDisplayName(Language.format(text.replaceAll(" ", "_")));
                        new FactionSettingsGui(player);
                    }
                    return AnvilGUI.Response.close();
                }).text(faction.getDisplayName())
                .itemLeft(new ItemStack(Material.GRAY_BANNER))
                .title("§eChange your name")
                .plugin(MainIF.getIF())
                .open(player);
    }

    public void changeMotd(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        new AnvilGUI.Builder().onClose((user) -> {
                })
                .onComplete((user, text) -> {
                    faction.setMotd(Language.format(text));
                    new FactionSettingsGui(player);
                    return AnvilGUI.Response.close();
                }).text(faction.getMotd())
                .itemLeft(new ItemStack(Material.GRAY_BANNER))
                .title("§eChange your motd")
                .plugin(MainIF.getIF())
                .open(player);
    }

    public void changeTag(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        new AnvilGUI.Builder().onClose((user) -> {
                })
                .onComplete((user, text) -> {
                    Integer tagLen = ConfigManager.getValue("faction.maxTagLen");
                    if (tagLen == null) tagLen = 3;

                    String fText = Language.format(text);
                    fText = fText.substring(0, Math.min(fText.length(), tagLen));
                    faction.setTag(fText);
                    new FactionSettingsGui(player);
                    return AnvilGUI.Response.close();
                }).text(faction.getTag())
                .itemLeft(new ItemStack(Material.GRAY_BANNER))
                .title("§eChange your tag")
                .plugin(MainIF.getIF())
                .open(player);
    }

    public void openMembers(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        AsyncTask.runLaterSync(0, () -> new MemberGui(player, faction, new GuiSettings()
                .setQuitGui(() -> new FactionSettingsGui(player))));
    }

    public void openOnline(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        AsyncTask.runLaterSync(0, () -> new OnlineGUI(player, faction, () ->
                new FactionSettingsGui(player)));
    }

    public void openRanks(Player player) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        AsyncTask.runLaterSync(0, () -> new RankGui(player, faction, () ->
                new FactionSettingsGui(player)));
    }
}
