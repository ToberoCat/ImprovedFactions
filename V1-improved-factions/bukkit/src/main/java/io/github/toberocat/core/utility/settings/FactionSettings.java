package io.github.toberocat.core.utility.settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.player.SettingActions;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.color.FactionColors;
import io.github.toberocat.core.utility.settings.type.CallbackSettings;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Material;

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
        SettingActions actions = new SettingActions();
        add(new EnumSetting(OpenType.values(), "openType",
                Utility.createItem(Material.END_PORTAL_FRAME, "&eChange join type", new String[]{
                        "&8Allow other people to", "&8find you using", "&8/f join"
                })));

        add(new CallbackSettings(actions::renameFaction, "change?rename", "Input",
                Utility.createItem(Material.OAK_SIGN, "&eRename faction",
                        new String[]{"&8Change your name", "&7Tip: Use color codes"})));

        add(new CallbackSettings(actions::changeMotd, "change?motd", "Input",
                Utility.createItem(Material.SPRUCE_SIGN, "&eSet Motd",
                        new String[]{"&8Change your motd", "&7Tip: Use color codes"})));

        add(new CallbackSettings(actions::changeTag, "change?tag", "Input",
                Utility.createItem(Material.BIRCH_SIGN, "&eSet Tag",
                        new String[]{"&8Change your tag", "&cNote: &7A tag can only have " +
                                ConfigManager.getValue("faction.maxTagLen") +
                                " or less characters", "&7Tip: Use color codes"})));


        add(new EnumSetting(FactionColors.values(), "universal_color", Utility.createItem(Material.GREEN_DYE,
                "Universal color")));

        add(new CallbackSettings(actions::openMembers, "open?members", "Gui",
                Utility.getSkull("https://textures.minecraft.net/texture/a05a32bbab24f84db679df637b7769fbf8f26e8f5765d0fbdbdea288cb6548f8",
                        1, "Manage your members", "&8Manage your members",
                        "&7Tip: Click on the head to manage them individually")));

        add(new CallbackSettings(actions::openOnline, "open?online", "Gui",
                Utility.getSkull("https://textures.minecraft.net/texture/fa6d51c22c8958285c00aaaf93b621c15be10aa04838afe1d89cd9c3603144df",
                        1, "See online members", "&8See your members online time")));

        add(new CallbackSettings(actions::openRanks, "open?ranks", "Gui",
                Utility.getSkull("https://textures.minecraft.net/texture/5b34b4896e434ec4ef1669d6343b6da06cd830dc92927d61e3d883017683c422",
                        1, "Manage ranks", "&8Manage your player ranks")));
    }


    public static void add(Setting setting) {
        DEFAULT_SETTINGS.put(setting.getSettingName(), setting);
    }

    public Map<String, Setting> getFactionSettings() {
        return factionSettings;
    }

    public void setFactionSettings(Map<String, Setting> factionSettings) {
        this.factionSettings = factionSettings;
        DEFAULT_SETTINGS.forEach((key, setting) -> {
            if (!factionSettings.containsKey(key)) factionSettings.put(key, setting);
        });
    }
}
