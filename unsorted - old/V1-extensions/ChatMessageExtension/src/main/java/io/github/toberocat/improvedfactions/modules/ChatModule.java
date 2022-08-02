package io.github.toberocat.improvedfactions.modules;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.modules.FactionModule;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.ChatMessageExtension;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatModule extends FactionModule {

    public ChatModule(Faction faction) {
        super(faction);
    }

    public static void send(Player player, String message) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        if (!faction.getModules().containsKey(ChatMessageExtension.MODULE_REGISTRY))
            faction.getModules().put(ChatMessageExtension.MODULE_REGISTRY, new ChatModule(faction));

        if (faction.getModules().get(ChatMessageExtension.MODULE_REGISTRY) instanceof ChatModule module)
            module.sendPlayerMessage(player, message);
    }

    public static void sendAlly(Player player, String message) {
        Faction faction = FactionUtility.getPlayerFaction(player);
        if (faction == null) return;

        if (!faction.getModules().containsKey(ChatMessageExtension.MODULE_REGISTRY))
            faction.getModules().put(ChatMessageExtension.MODULE_REGISTRY, new ChatModule(faction));

        if (faction.getModules().get(ChatMessageExtension.MODULE_REGISTRY) instanceof ChatModule module)
            module.sendAllyMessage(player, message);
    }

    public static void broadcast(@NotNull Faction faction, String message) {
        ChatMessageExtension
                .getModule(faction)
                .broadcast(Language.format(message));
    }

    public void sendPlayerMessage(Player player, String message) {
        sendMessage(String.format("§7[§e%s Chat§7] §f%s: §r%s", faction.getDisplayName(), player.getName(), message.trim()));
    }

    private void sendMessage(String message) {
        AsyncTask.run(() -> {
            for (Player member : faction.getFactionMemberManager().getOnlinePlayers()) {
                if (!faction.hasPermission(member, ChatMessageExtension.PERMISSION_FACTION_CHAT_READ)) continue;
                member.sendMessage(message);
            }
        });
    }

    public void broadcast(String message) {
        sendMessage(String.format("§7[§e%s§7] §r%s", faction.getDisplayName(), message.trim()));
    }

    public void sendAllyMessage(Player player, String message) {
        String msg = String.format("§7[§bAlly Chat§7]§f %s: §r%s", player.getDisplayName(), message.trim());
        sendMessage(msg);

        for (String allied : faction.getRelationManager().getAllies()) {
            Faction ally = FactionUtility.getFactionByRegistry(allied);
            if (ally == null) continue;

            ChatMessageExtension.getModule(ally).sendMessage(msg);
        }
    }
}
