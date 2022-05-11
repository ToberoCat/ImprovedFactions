package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class FactionMapSubCommand extends AutoSubCommand {
    public FactionMapSubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        super(subCommand, permission, descriptionKey, manager);
    }

    @Override
    public String getEnabledKey() {
        return null;
    }

    @Override
    public String getDisabledKey() {
        return null;
    }

    @Override
    public void onSingle(Player player) {
        Chunk center = player.getLocation().getChunk();
        int dstH = MainIF.getConfigManager().getValue("general.mapViewDistanceW");
        int dstW = MainIF.getConfigManager().getValue("general.mapViewDistanceH");

        int leftTopX = center.getX() - dstW/2;
        int leftTopZ = center.getZ() - dstH/2;

        int rightDownX = center.getX() + dstW/2;
        int rightDownZ = center.getZ() + dstH/2;

        Chunk[][] chunks = new Chunk[dstW][dstH];

        for (int x = rightDownX; x > leftTopX; x--) {
            for (int z = rightDownZ; z > leftTopZ; z--) {
                chunks[x-leftTopX][z-leftTopZ] = player.getLocation().getWorld().getChunkAt(x, z);
            }
        }

        TextComponent map = new TextComponent(Language.getPrefix() + "§fMap for §7"+ center.getX() + "; " + center.getZ() +"\n");
        for (int i = 0; i < dstW; i++) {
            map.addExtra(Language.getPrefix());
            for (int j = 0; j < dstH; j++) {
                //MapSubCommand.getChunk(chunks[i][j],player, map::addExtra);
            }
            if (i < (dstW-1)) {
                map.addExtra("\n");
            }
        }
        player.spigot().sendMessage(map);
    }

    public static void getChunk(Chunk chunk, Player player) {
        String factionRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(chunk);
        String color = "";
        String hover;
        String symbol = "■";
        if (factionRegistry == null) { //Wildness
            color = "§2";
            hover = color + "Wildness";
        }
        else {
            color = factionRegistry.equals(FactionUtility.getPlayerFactionRegistry(player)) ? "§a" : "§c";
            hover = color + FactionUtility.getFactionByRegistry(factionRegistry).getDisplayName();
        }

        if (player.getLocation().getChunk() == chunk) {
            hover = color +  "You";
            symbol = "o";
        }

        TextComponent com = new TextComponent(color + symbol);
        com.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                hover+"\n§7"+chunk.getX()+"; "+chunk.getZ()).create()));
        //callback.Callback(com);
    }
}
