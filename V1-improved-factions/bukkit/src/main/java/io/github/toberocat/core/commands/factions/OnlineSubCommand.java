package io.github.toberocat.core.commands.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.gui.faction.OnlineGUI;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OnlineSubCommand extends SubCommand {
    public OnlineSubCommand() {
        super("online", "command.online.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        Bukkit.getScheduler().runTask(MainIF.getIF(), () -> { openGui(player); });
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return null;
    }

    private void openGui(@NotNull Player player) {
        String registry = FactionHandler.getPlayerFaction(player);
        if (registry == null) return;

        try {
            Faction<?> faction = FactionHandler.getFaction(registry);
            new OnlineGUI(player, faction, () -> {});
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        }
    }
}
