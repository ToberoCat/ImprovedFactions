package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.player.PlayerSettings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerMoveListener implements Listener {

    public static Map<UUID, HashMap<String, Consumer<Player>>> MOVE_OPERATIONS = new HashMap<>();

    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        if (Utility.isDisabled(event.getPlayer().getWorld())) return;

        Chunk from = event.getFrom().getChunk();
        Chunk to = Objects.requireNonNull(event.getTo()).getChunk();
        Player player = event.getPlayer();

        if (from != to) {
            String fromRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(from);
            String toRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(to);

            if (MOVE_OPERATIONS.containsKey(player.getUniqueId())) {
                HashMap<String, Consumer<Player>> use = MOVE_OPERATIONS.get(player.getUniqueId());
                AsyncTask.runLaterSync(1, () -> {
                    for (Consumer<Player> run : use.values()) run.accept(player);
                });
            }

            if (toRegistry == null) {
                if (fromRegistry != null) {
                    PlayerSettings settings = PlayerSettings.getSettings(player.getUniqueId());
                    if (!(Boolean) settings.getSetting("displayTitle").getSelected()) return;
                    PlayerSettings.TitlePosition position = PlayerSettings.TitlePosition.values()[(int) settings.getSetting("titlePosition").getSelected()];
                    send(position, player, Language.getMessage("territory.wilderness", player), "");
                }
                return;
            }

            if (ClaimManager.isManageableZone(toRegistry)) {
                if (!toRegistry.equals(fromRegistry)) {
                    String display = ClaimManager.getDisplay(toRegistry);
                    if (display.isEmpty()) return;

                    display = Language.getMessage(display, player);
                    PlayerSettings settings = PlayerSettings.getSettings(player.getUniqueId());
                    if (!(Boolean) settings.getSetting("displayTitle").getSelected()) return;
                    PlayerSettings.TitlePosition position = PlayerSettings.TitlePosition.values()[(int) settings.getSetting("titlePosition").getSelected()];
                    send(position, player, display, "");
                }
                return;
            }

            if (!FactionManager.doesFactionExist(toRegistry)) {
                MainIF.getIF().getClaimManager().removeProtection(to);
                return;
            }

            if (!toRegistry.equals(fromRegistry)) display(player, toRegistry);

        }
    }

    private void display(Player player, String toRegistry) {
        PlayerSettings settings = PlayerSettings.getSettings(player.getUniqueId());
        if (!(Boolean) settings.getSetting("displayTitle").getSelected()) return;

        PlayerSettings.TitlePosition position = PlayerSettings.TitlePosition.values()[(int) settings.getSetting("titlePosition").getSelected()];

        sendTitle(position, player, toRegistry);
    }

    private void sendTitle(PlayerSettings.TitlePosition pos, Player player, String registry) {
        AsyncTask.run(() -> {
            if (registry == null) {
                send(pos, player, Language.getMessage("territory.wilderness", player), "");
                return;
            }

            Faction faction = FactionManager.getFactionByRegistry(registry);
            Faction playerFaction = FactionManager.getPlayerFaction(player);

            String text = null;

            if (faction != null) text = faction.getDisplayName();
            else if (registry.equals(ClaimManager.SAFEZONE_REGISTRY))
                text = Language.getMessage("territory.safezone", player);
            else if (registry.equals(ClaimManager.WARZONE_REGISTRY))
                text = Language.getMessage("territory.warzone", player);
            else if (registry.equals(ClaimManager.UNCLAIMABLE_REGISTRY)) return;

            String relation = "&e";
            if (playerFaction != null && faction != null) {
                if (playerFaction.getRegistryName().equals(registry)) relation = "&a";
                else if (playerFaction.getRelationManager().getAllies().contains(faction.getRegistryName()))
                    relation = "&b";
                else if (playerFaction.getRelationManager().getEnemies().contains(faction.getRegistryName()))
                    relation = "&c";
            }

            if (text == null)
                text = Language.getMessage("territory.wilderness", player); // There is still a claim, but the faction got deleted
            send(pos, player, text, relation);
        });
    }

    private void send(PlayerSettings.TitlePosition pos, Player player, String text, String relation) {
        Parseable[] parses = new Parseable[]{
                new Parseable("{territory}", text),
                new Parseable("{relation}", relation == null || relation.length() == 0 ? "&r" : relation)
        };

        switch (pos) {
            case CHAT -> Language.sendMessage("territory.entered.chat", player, parses);
            case ACTIONBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Language.getMessage("territory.entered.actionbar",
                            player, parses)));
            case TITLE -> player.sendTitle(Language.getMessage("territory.entered.title",
                    player, parses), "", 5, 20, 5);
            case SUBTITLE -> player.sendTitle(" ", Language.getMessage("territory.entered.subtitle",
                    player, parses), 5, 20, 5);
        }
    }
}
