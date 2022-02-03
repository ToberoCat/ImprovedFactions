package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.history.History;
import io.github.toberocat.core.utility.history.territory.Territory;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        Chunk from = event.getFrom().getChunk();
        Chunk to = Objects.requireNonNull(event.getTo()).getChunk();
        Player player = event.getPlayer();

        if (from != to) {
            String fromRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(from);
            String toRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(to);

            if (toRegistry == null && fromRegistry == null) return;
            if (fromRegistry == null) {
                display(player, "wildness", toRegistry, from, to);
                return;
            } else if (toRegistry == null) {
                display(player, fromRegistry, "wildness", from, to);
                return;
            }

            if (!fromRegistry.equals(toRegistry)) {
                display(player, fromRegistry, toRegistry, from, to);
            }
        }
    }

    private void display(Player player, String fromRegistry, String toRegistry, Chunk from, Chunk to) {
        History.logTerritorySwitch(player, new Territory(fromRegistry, from),
                new Territory(toRegistry, to));

        PlayerSettings settings = PlayerSettings.getSettings(player.getUniqueId()).getPaired();
        if (!(Boolean) settings.getSetting("displayTitle").getSelected()) return;

        PlayerSettings.TitlePosition position = PlayerSettings.TitlePosition.valueOf(settings.getSetting("titlePosition").getSelected().toString());

        sendTitle(position, player, MainIF.getIF().getClaimManager().getFactionRegistry(to));
    }

    private void sendTitle(PlayerSettings.TitlePosition pos, Player player, String registry) {
        Faction faction = FactionUtility.getFactionByRegistry(registry);
        Faction playerFaction = FactionUtility.getPlayerFaction(player);

        AsyncCore.Run(() -> {
            String text;

            if (faction != null) {
                text = faction.getDisplayName();
            } else {
                text = Language.getMessage(LangMessage.TERRITORY_WILDERNESS, player);
            }

            String relation = "&e";
            if (playerFaction != null && faction != null) {
                if (playerFaction.getRegistryName().equals(registry)) {
                    relation = "&a";
                } else if (playerFaction.getRelationManager().getAllies().contains(faction.getRegistryName())) {
                    relation = "&b";
                } else if (playerFaction.getRelationManager().getEnemies().contains(faction.getRegistryName())) {
                    relation = "&c";
                }
            }

            Parseable[] parses = new Parseable[] {new Parseable("{territory}", text),
                    new Parseable("{relation}", relation)};

            switch (pos) {
                case CHAT -> Language.sendMessage(LangMessage.TERRITORY_ENTERED_CHAT, player, parses);
                case ACTIONBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Language.getMessage(LangMessage.TERRITORY_ENTERED_ACTIONBAR,
                                player, parses)));
                case TITLE -> player.sendTitle(Language.getMessage(LangMessage.TERRITORY_ENTERED_TITLE,
                        player, parses), "", 5, 20, 5);
                case SUBTITLE -> player.sendTitle(" ", Language.getMessage(LangMessage.TERRITORY_ENTERED_SUBTITLE,
                        player, parses), 5, 20, 5);
            }
        });
    }
}
