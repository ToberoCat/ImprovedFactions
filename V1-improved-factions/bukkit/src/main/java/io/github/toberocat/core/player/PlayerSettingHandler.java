package io.github.toberocat.core.player;

import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.data.Database;
import io.github.toberocat.core.utility.data.Table;
import io.github.toberocat.core.utility.data.access.AbstractAccess;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.data.database.DatabaseAccess;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import io.github.toberocat.core.utility.exceptions.setting.SettingNotFoundException;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.settings.type.HiddenSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSettingHandler implements Listener {
    public static final Map<String, Setting> DEFAULT_SETTINGS = new HashMap<>();
    protected static final Map<UUID, PlayerSettings> CACHED_SETTINGS = new HashMap<>();

    @EventHandler
    private void leave(@NotNull PlayerQuitEvent event) {
        saveToStorage(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent event) {
        createSetting(event.getPlayer().getUniqueId());
    }

    public void register() {
        addDefault(new BoolSetting("bossBars", true,
                Utility.createItem(Material.HEART_OF_THE_SEA, "&eDisplay boss bar", new String[]{
                        "&8The bossbar will appear when", "&8your faction power changes.", "&8Through losing or gaining power"
                })));
        addDefault(new BoolSetting("hideCommandDescription", false, Utility.createItem(Material.COMMAND_BLOCK,
                "&eHide command descriptions", new String[]{"&8You think the auto", "&8descriptions annoy you?",
                        "&8Disable them"})));
        addDefault(new BoolSetting("displayTitle", true, Utility.createItem(Material.NAME_TAG,
                "&eDisplay territory titles")));
        addDefault(new EnumSetting(PlayerSettings.TitlePosition.values(), "titlePosition", Utility.createItem(
                Material.END_PORTAL_FRAME, "&eDisplay position", new String[]{"&8Change where you want",
                        "&8Territory changes be announced"})));
        addDefault(new BoolSetting("showNotifications", true,
                Utility.createItem(Material.BOOK, "&eShow notifications", new String[]{
                        "§8Display messages when", "§8your faction status", "§8has been changed"
                })));

        addDefault(new BoolSetting("showTips", true,
                Utility.createItem(Material.FEATHER, "&eShow tips")));

        addDefault(new HiddenSetting<>("factionJoinTimeout", "-1"));


        for (Player player : Bukkit.getOnlinePlayers()) createSetting(player.getUniqueId());
    }

    public static void dispose() {
        CACHED_SETTINGS.keySet().forEach(PlayerSettingHandler::saveToStorage);
        CACHED_SETTINGS.clear();
    }

    public static void addDefault(Setting<?> setting) {
        DEFAULT_SETTINGS.put(setting.getSettingName(), setting);
    }


    public static PlayerSettings createSetting(@NotNull UUID player) {
        PlayerSettings settings = AbstractAccess.isAccess(DatabaseAccess.class) ? readSettingsMsql() :
                readSettingsJson();

        if (settings == null || settings.getPlayerSettings() == null) {
            Debugger.log("Couldn't load player settings. Creating new one");
            return new PlayerSettings(player);
        } else {
            Debugger.log("Loaded player settings for " + player);
            Setting.populateSettings(DEFAULT_SETTINGS, settings.getPlayerSettings());
        }
    }

    private static PlayerSettings readSettingsMsql() {
        MySqlDatabase database = DatabaseAccess.accessPipeline(DatabaseAccess.class).database();

    }

    private static PlayerSettings readSettingsJson(@NotNull UUID player) {
        FileAccess access = FileAccess.accessPipeline(FileAccess.class);
        if (!access.has(Table.PLAYERS, player.toString())) return null;
        return access.read(Table.PLAYERS, player.toString());
    }

    public static void saveToStorage(@NotNull UUID player) {
        if (AbstractAccess.isAccess(DatabaseAccess.class)) {
            MySqlDatabase access = DatabaseAccess.accessPipeline(DatabaseAccess.class).database();
            // ToDo: Implement sql player setting saving
        } else {
            FileAccess.accessPipeline(FileAccess.class).write(Table.PLAYERS, player.toString(), getSettings(player));
        }
        Debugger.log("Saved " + player + " player settings");
        CACHED_SETTINGS.remove(player);
    }

    public static PlayerSettings getSettings(UUID uuid) {
        if (!CACHED_SETTINGS.containsKey(uuid)) createSetting(uuid);

        return CACHED_SETTINGS.get(uuid);
    }
}
