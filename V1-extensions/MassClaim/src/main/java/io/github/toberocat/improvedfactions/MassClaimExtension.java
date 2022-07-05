package io.github.toberocat.improvedfactions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.improvedfactions.command.MassSubCommand;
import io.github.toberocat.improvedfactions.listener.InventoryListener;
import io.github.toberocat.improvedfactions.listener.ItemUseListener;
import io.github.toberocat.improvedfactions.listener.PlayerLeaveListener;
import io.github.toberocat.improvedfactions.wand.PositionWand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MassClaimExtension extends Extension {

    public static Map<UUID, Position> POSITIONS = new HashMap<>();

    @Override
    protected void onEnable(MainIF plugin) {
        registerListener(plugin);
        new PositionWand().register();

        AsyncTask.runLaterSync(1, () -> {
            FactionCommand.subCommands.add(new MassSubCommand(plugin.getClaimManager()));
        });
    }

    @Override
    protected void onDisable(MainIF plugin) {
        PositionWand.dispose();
    }

    public void registerListener(MainIF plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ItemUseListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryListener(), plugin);
    }
}
