package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.command.AutoSubCommand;
import io.github.toberocat.core.utility.language.Language;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

import static java.lang.Math.ceil;

public class FactionMapSubCommand extends AutoSubCommand {
    public FactionMapSubCommand() {
        super("map", "command.faction.map.description", false);
    }

    public static TextComponent getChunk(Chunk chunk, Player player) {
        if (chunk == null) return new TextComponent("");

        String factionRegistry = MainIF.getIF().getClaimManager().getFactionRegistry(chunk);
        String color = "";
        String hover;
        String symbol = "■";
        if (factionRegistry == null) { //Wildness
            color = "§2";
            hover = color + "Wildness";
        } else {
            String playerRegistry = FactionUtility.getPlayerFactionRegistry(player);
            if (playerRegistry == null) return new TextComponent();

            color = factionRegistry.equals(playerRegistry) ? "§a" : "§c";

            Faction registryFaction = FactionUtility.getFactionByRegistry(factionRegistry);
            if (registryFaction == null) return new TextComponent();

            hover = color + registryFaction.getDisplayName();
        }

        if (player.getLocation().getChunk() == chunk) {
            hover = color + "You";
            symbol = "o";
        }

        TextComponent com = new TextComponent(color + symbol);
        com.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(
                hover + "\n§7" + chunk.getX() + "; " + chunk.getZ()).create()));

        return com;
    }

    @Override
    public String getEnabledKey() {
        return "command.faction.map.auto-enable";
    }

    @Override
    public String getDisabledKey() {
        return "command.faction.map.auto-disable";
    }

    @Override
    public void onSingle(Player player) {
        Chunk center = player.getLocation().getChunk();
        int dstH = MainIF.getConfigManager().getValue("general.mapViewDistanceW");
        int dstW = MainIF.getConfigManager().getValue("general.mapViewDistanceH");

        int leftCornerX = (int) (center.getX() - ceil(dstW / 2f));
        int leftCornerZ = (int) (center.getZ() - ceil(dstH / 2f));
        Chunk[][] chunks = new Chunk[dstW + 1][dstH + 1];

        World world = player.getLocation().getWorld();
        if (world == null) return;

        for (int x = 0; x < dstW; x++)
            for (int z = 0; z < dstW; z++)
                chunks[x][z] = world.getChunkAt(leftCornerX + x, leftCornerZ + z);


        player.spigot().sendMessage(new TextComponent(Language.getPrefix(player) + "§fMap for §7" +
                center.getX() + "; " + center.getZ() + "\n"));
        for (int i = 0; i < dstW; i++) {
            TextComponent map = new TextComponent(Language.getPrefix(player));
            for (int j = 0; j < dstH; j++)
                map.addExtra(getChunk(chunks[i][j], player));

            player.spigot().sendMessage(map);
        }
    }
}
