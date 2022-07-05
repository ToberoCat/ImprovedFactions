package io.github.toberocat.improvedfactions.commands.factionCommands;

import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.listeners.OnChunkEntered;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import io.github.toberocat.improvedfactions.utility.TCallback;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MapSubCommand extends SubCommand {
    public static List<UUID> AUTO_MAPS = new ArrayList<>();

    public MapSubCommand() {
        super("map", LangMessage.MAP_DESCRIPTION);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 1 && args[0].equals("auto")) {
            if (AUTO_MAPS.contains(player.getUniqueId())) {
                player.sendMessage(Language.getPrefix() + "§c§lDisabled§f auto map");
                AUTO_MAPS.remove(player.getUniqueId());
            } else {
                AUTO_MAPS.add(player.getUniqueId());
                player.sendMessage(Language.getPrefix() + "§a§lEnabled§f auto map");
                OnChunkEntered.sendMap(player);
            }
        } else {
            OnChunkEntered.sendMap(player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return List.of("auto");
    }

    public static void getChunk(Chunk chunk, Player player, TCallback<TextComponent> callback) {
        Faction faction = ChunkUtils.GetFactionClaimedChunk(chunk);
        String color = "";
        String hover;
        String symbol = "■";
        if (faction == null) { //Wildness
            color = "§2";
            hover = color + "Wildness";
        }
        else {
            color = faction == FactionUtils.getFaction(player) ? "§a" : "§c";
            hover = color + faction.getDisplayName();
        }

        if (player.getLocation().getChunk() == chunk) {
            hover = color +  "You";
            symbol = "o";
        }

        TextComponent com = new TextComponent(color + symbol);
        com.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                hover+"\n§7"+chunk.getX()+"; "+chunk.getZ()).create()));
        callback.Callback(com);
    }
}
