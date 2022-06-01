package io.github.toberocat.improvedfactions;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.rank.members.*;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.improvedfactions.commands.HomeSubCommand;
import io.github.toberocat.improvedfactions.commands.SetHomeSubCommand;
import io.github.toberocat.improvedfactions.listener.LoadFactionListener;
import io.github.toberocat.improvedfactions.listener.PlayerMoveListener;
import io.github.toberocat.improvedfactions.module.HomeModule;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public final class HomeExtension extends Extension {

    public static final HashMap<UUID, BukkitRunnable> TELEPORTING_PLAYERS = new HashMap<>();

    public static final String HOME_MODULE_REGISTRY = "home_module";
    public static final String HOME_EXTENSION_REGISTRY = "home_extension";

    public static final String SET_HOME_PERMISSION = "permission.set_home";
    public static final String HOME_PERMISSION = "permission.home";

    public static int SECONDS_TILL_TELEPORT;
    public static boolean RESET_ON_MOVE;

    @Override
    protected void onEnable(MainIF plugin) {
        JsonUtility.OBJECT_MAPPER.registerSubtypes(new NamedType(HomeModule.class, HOME_MODULE_REGISTRY));

        reloadConfigs();

        registerListener(plugin);
        registerCommands();
        registerPermissions();
    }

    @Override
    public void reloadConfigs() {
        setConfigDefaultValue("secondsTillTeleport", 3);
        setConfigDefaultValue("resetOnMove", true);

        SECONDS_TILL_TELEPORT = configValue("secondsTillTeleport");
        RESET_ON_MOVE = configValue("resetOnMove");
    }

    private void registerListener(MainIF plugin) {
        plugin.registerListener(new LoadFactionListener());
        plugin.registerListener(new PlayerMoveListener());
    }

    private void registerPermissions() {
        FactionPerm.registerPermission(new RankSetting(SET_HOME_PERMISSION, new String[]{
                OwnerRank.registry, ModeratorRank.registry, AdminRank.registry
        }, Utility.createItem(Material.RED_BED, "&eSet home permission", new String[]{
                "&8Allows the user to set the", "&8entire faction home for everyone"
        })));

        FactionPerm.registerPermission(new RankSetting(HOME_PERMISSION, new String[]{
                OwnerRank.registry, ModeratorRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry
        }, Utility.createItem(Material.YELLOW_BED, "&eTeleport to home permission", new String[]{
                "&8Allows the user to teleport to", "&8the faction home"
        })));
    }

    private void registerCommands() {
        FactionCommand.subCommands.add(new SetHomeSubCommand());
        FactionCommand.subCommands.add(new HomeSubCommand());
    }
}
