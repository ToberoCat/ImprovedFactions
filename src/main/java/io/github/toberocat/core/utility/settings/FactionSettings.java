package io.github.toberocat.core.utility.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.gui.faction.FactionSettingsGui;
import io.github.toberocat.core.gui.faction.MemberGui;
import io.github.toberocat.core.gui.faction.OnlineGUI;
import io.github.toberocat.core.gui.faction.RankGui;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.color.FactionColors;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import io.github.toberocat.core.utility.settings.type.CallbackSettings;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FactionSettings {
    @JsonIgnore
    public static final Map<String, Setting> DEFAULT_SETTINGS = new HashMap<>();

    @JsonIgnore
    private Map<String, Setting> factionSettings;

    public FactionSettings() {
        this.factionSettings = new HashMap<>(DEFAULT_SETTINGS);
    }

    @JsonIgnore
    public static void register() {
        add(new EnumSetting(OpenType.values(), "openType",
                Utility.createItem(Material.END_PORTAL_FRAME, "&eChange join type", new String[]{
                        "&8Allow other people to", "&8find you using", "&8/f join"
                })));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            new AnvilGUI.Builder().onClose((user) -> {
                    })
                    .onComplete((user, text) -> {
                        faction.setDisplayName(Language.format(text.replaceAll(" ", "_")));
                        return AnvilGUI.Response.close();
                    }).text(faction.getDisplayName())
                    .itemLeft(new ItemStack(Material.GRAY_BANNER))
                    .title("§eChange your name")
                    .plugin(MainIF.getIF())
                    .open(player);
        }, "change?rename", "Input", Utility.createItem(Material.OAK_SIGN, "&eRename faction", new String[]{"&8Change your name",
                "&7Tip: Use color codes"})));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            new AnvilGUI.Builder().onClose((user) -> {
                    })
                    .onComplete((user, text) -> {
                        faction.setMotd(Language.format(text));
                        return AnvilGUI.Response.close();
                    }).text(faction.getMotd())
                    .itemLeft(new ItemStack(Material.GRAY_BANNER))
                    .title("§eChange your motd")
                    .plugin(MainIF.getIF())
                    .open(player);
        }, "change?motd", "Input", Utility.createItem(Material.SPRUCE_SIGN, "&eSet Motd", new String[]{"&8Change your motd",
                "&7Tip: Use color codes"})));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            new AnvilGUI.Builder().onClose((user) -> {
                    })
                    .onComplete((user, text) -> {
                        Integer tagLen = MainIF.getConfigManager().getValue("faction.maxTagLen");
                        if (tagLen == null) tagLen = 3;

                        String fText = Language.format(text);
                        fText = fText.substring(0, Math.min(fText.length(), tagLen));
                        faction.setTag(fText);
                        return AnvilGUI.Response.close();
                    }).text(faction.getTag())
                    .itemLeft(new ItemStack(Material.GRAY_BANNER))
                    .title("§eChange your tag")
                    .plugin(MainIF.getIF())
                    .open(player);
        }, "change?tag", "Input", Utility.createItem(Material.BIRCH_SIGN, "&eSet Tag", new String[]{"&8Change your tag", "&cNote: &7A tag can only have " + MainIF.getConfigManager().getValue("faction.maxTagLen") + " or less characters",
                "&7Tip: Use color codes"})));


        add(new EnumSetting(FactionColors.values(), "universal_color", Utility.createItem(Material.GREEN_DYE,
                "Universal color")));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            AsyncTask.runLaterSync(0, () -> new MemberGui(player, faction, new GUISettings()
                    .setQuitIcon(true).setQuitCallback(() -> new FactionSettingsGui(player))));
        }, "open?members", "Gui", Utility.getSkull("https://textures.minecraft.net/texture/a05a32bbab24f84db679df637b7769fbf8f26e8f5765d0fbdbdea288cb6548f8",
                1, "Manage your members", "&8Manage your members",
                "&7Tip: Click on the head to manage them individually")));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            AsyncTask.runLaterSync(0, () -> {
                new OnlineGUI(player, faction, new GUISettings().setQuitIcon(true).setQuitCallback(() ->
                        new FactionSettingsGui(player)));
            });
        }, "open?online", "Gui", Utility.getSkull("http://textures.minecraft.net/texture/fa6d51c22c8958285c00aaaf93b621c15be10aa04838afe1d89cd9c3603144df",
                1, "See online members", "&8See your members online time")));

        add(new CallbackSettings((player) -> {
            Faction faction = FactionUtility.getPlayerFaction(player);
            if (faction == null) return;

            AsyncTask.runLaterSync(0, () -> new RankGui(player, faction, new GUISettings()
                    .setQuitIcon(true).setQuitCallback(() ->
                            new FactionSettingsGui(player))));
        }, "open?ranks", "Gui", Utility.getSkull("http://textures.minecraft.net/texture/5b34b4896e434ec4ef1669d6343b6da06cd830dc92927d61e3d883017683c422",
                1, "Manage ranks", "&8Manage your player ranks")));

        boolean default_explosion = Boolean.TRUE.equals(MainIF.getConfigManager().getValue("general.allowExplosions"));

        add(new BoolSetting("explosions", default_explosion, Utility.createItem(Material.TNT, "Allow explosions",
                new String[]{"&8Enable & Disable explosions", "&8Like &cTnt"})));
    }

    public static void add(Setting setting) {
        DEFAULT_SETTINGS.put(setting.getSettingName(), setting);
    }

    public Map<String, Setting> getFactionSettings() {
        return factionSettings;
    }

    public void setFactionSettings(Map<String, Setting> factionSettings) {
        this.factionSettings = factionSettings;
    }
}
