package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.events.faction.FactionAllyEvent;
import io.github.toberocat.core.utility.events.faction.FactionJoinEvent;
import io.github.toberocat.core.utility.events.faction.FactionLeaveEvent;
import io.github.toberocat.core.utility.events.faction.power.FactionPowerEvent;
import io.github.toberocat.core.utility.events.faction.power.PowerCause;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static io.github.toberocat.improvedfactions.modules.ChatModule.broadcast;

public class FactionListener implements Listener {

    @EventHandler
    public void memberJoin(FactionJoinEvent event) {
        broadcast(event.getFaction(), "&e" + event.getPlayer().getDisplayName() + " joined your faction");
    }

    @EventHandler
    public void factionLeave(FactionLeaveEvent event) {
        broadcast(event.getFaction(), "&e" + event.getPlayer().getName() + " left your faction");
    }

    @EventHandler
    public void ally(FactionAllyEvent event) {
        broadcast(event.getFaction(), "&e" + event.getAllied().getDisplayName() +
                " &fis now your ally. Chat with them using /f chat");
        broadcast(event.getAllied(), "&e" + event.getFaction().getDisplayName() +
                " &faccepted your ally invite. Chat with them using /f chat");
    }

    @EventHandler
    public void powerChange(FactionPowerEvent event) {
        PowerCause cause = event.getCause();
        Faction faction = event.getFaction();
        int change = event.getChange();
        int current = faction.getPowerManager().getCurrentPower();
        int max = faction.getPowerManager().getMaxPower();

        switch (cause) {
            case PLAYER_KILLED -> broadcast(faction,
                    String.format("&bYour faction lost power. %d/%d", current, max));
            case REGENERATION_TICK -> broadcast(faction,
                    String.format("&bYour faction regenerated %d power", current));
            case MAX_POWER -> broadcast(faction,
                    String.format("&bYour faction maximum power changed to %d", max));
            case PLAYER_JOIN -> broadcast(faction,
                    String.format("&bYour faction gained %d maximum power", change));
            case PLAYER_LEFT -> broadcast(faction,
                    String.format("&bYour faction lost %d maximum power", change));
        }
    }

}
