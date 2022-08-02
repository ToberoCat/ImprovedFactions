package io.github.toberocat.improvedfactions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.modules.FactionModule;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.rank.members.AdminRank;
import io.github.toberocat.core.factions.rank.members.ElderRank;
import io.github.toberocat.core.factions.rank.members.MemberRank;
import io.github.toberocat.core.factions.rank.members.OwnerRank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.settings.type.RankSetting;
import io.github.toberocat.improvedfactions.command.ChatSubCommand;
import io.github.toberocat.improvedfactions.listeners.FactionListener;
import io.github.toberocat.improvedfactions.listeners.PlayerListener;
import io.github.toberocat.improvedfactions.modules.ChatModule;
import io.github.toberocat.improvedfactions.types.ChatType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class ChatMessageExtension extends Extension {
    public static final String PERMISSION_FACTION_CHAT_SEND = "chat.faction_send";
    public static final String PERMISSION_FACTION_CHAT_READ = "chat.faction_read";
    public static final String PERMISSION_ALLIES_CHAT_SEND = "chat.allies_send";
    public static final String PERMISSION_ALLIES_CHAT_READ = "chat.allies_read";

    public static Map<UUID, ChatType> PLAYER_CHAT_TYPE = new HashMap<>();

    public static void changeRotationChatType(Player player) {
        ChatType type = PLAYER_CHAT_TYPE.get(player.getUniqueId());

        type = ChatType.values()[(type.ordinal() + 1) % ChatType.values().length];

        Language.sendMessage("command.chat.switched", player, new Parseable("{chat}", type.getDisplay()));
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        PLAYER_CHAT_TYPE.replace(player.getUniqueId(), type);

        if (type == ChatType.FACTION && !faction.hasPermission(player, PERMISSION_ALLIES_CHAT_SEND)) {
            changeRotationChatType(player);
        } else if (type == ChatType.FACTION && !faction.hasPermission(player, PERMISSION_ALLIES_CHAT_SEND)) {
            changeRotationChatType(player);
        }
    }

    public static ChatModule getModule(Faction faction) {
        LinkedHashMap<String, FactionModule> modules = faction.getModules();
        if (!modules.containsKey(FactionModule))
            modules.put(MODULE_REGISTRY, new ChatModule(faction));

        return (ChatModule) modules.get(MODULE_REGISTRY);
    }

    @Override
    protected void onEnable(MainIF plugin) {
        registerPermissions();
        registerListeners(plugin);

        FactionCommand.subCommands.add(new ChatSubCommand());

        for (Player on : Bukkit.getOnlinePlayers()) PLAYER_CHAT_TYPE.put(on.getUniqueId(), ChatType.PUBLIC);
    }

    @Override
    protected void onDisable(MainIF plugin) {
        PLAYER_CHAT_TYPE.clear();
    }

    private void registerListeners(MainIF plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new FactionListener(), plugin);
    }

    private void registerPermissions() {
        FactionPerm.DEFAULT_RANKS.put(PERMISSION_FACTION_CHAT_SEND, new RankSetting(PERMISSION_FACTION_CHAT_SEND, new String[]{
                OwnerRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry,
        }, Utility.createItem(Material.WRITABLE_BOOK, "&eSend faction chat message")));
        FactionPerm.DEFAULT_RANKS.put(PERMISSION_FACTION_CHAT_READ, new RankSetting(PERMISSION_FACTION_CHAT_READ, new String[]{
                OwnerRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry,
        }, Utility.createItem(Material.KNOWLEDGE_BOOK, "&eRead faction chat message")));

        FactionPerm.DEFAULT_RANKS.put(PERMISSION_ALLIES_CHAT_SEND, new RankSetting(PERMISSION_ALLIES_CHAT_SEND, new String[]{
                OwnerRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry,
        }, Utility.createItem(Material.WRITABLE_BOOK, "&eSend ally faction chat message")));
        FactionPerm.DEFAULT_RANKS.put(PERMISSION_ALLIES_CHAT_READ, new RankSetting(PERMISSION_ALLIES_CHAT_READ, new String[]{
                OwnerRank.registry, AdminRank.registry, ElderRank.registry, MemberRank.registry,
        }, Utility.createItem(Material.KNOWLEDGE_BOOK, "&eRead ally faction chat message")));
    }
}
