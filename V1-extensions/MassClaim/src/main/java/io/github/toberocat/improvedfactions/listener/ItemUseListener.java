package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import io.github.toberocat.improvedfactions.command.MassClaimPos1;
import io.github.toberocat.improvedfactions.command.MassClaimPos2;
import io.github.toberocat.improvedfactions.wand.PositionWand;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import static io.github.toberocat.improvedfactions.MassClaimExtension.POSITIONS;

public class ItemUseListener implements Listener {

    @EventHandler
    public void use(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!PositionWand.USER_ITEMS.contains(player.getUniqueId())) return;
        if (event.getClickedBlock() == null) return;

        event.setCancelled(true);

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK -> {
                MassClaimPos1.pos1(player, event.getClickedBlock().getLocation());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent("§aPosition 1 set"));
                sendMessage(player);
            }
            case RIGHT_CLICK_BLOCK -> {
                MassClaimPos2.pos2(player, event.getClickedBlock().getLocation());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent("§aPosition 2 set"));
                sendMessage(player);
            }
        }
    }

    private void sendMessage(Player player) {
        if (!POSITIONS.containsKey(player.getUniqueId())) return;

        if (POSITIONS.get(player.getUniqueId()).getPos1() != null &&
                POSITIONS.get(player.getUniqueId()).getPos2() != null) {

           TextComponent msg = new TextComponent(Language.getMessage("confirm.claiming", player));
           msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f mass claim"));

           player.spigot().sendMessage(msg);
        }
    }
}
