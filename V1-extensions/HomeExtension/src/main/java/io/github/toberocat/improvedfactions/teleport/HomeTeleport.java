package io.github.toberocat.improvedfactions.teleport;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.bossbar.SimpleBar;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.improvedfactions.HomeExtension;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HomeTeleport extends BukkitRunnable {
    private final Player player;
    private final Location home;
    private final SimpleBar bar;

    private int left;

    public HomeTeleport(Player player, Location home, int seconds) {
        this.player = player;
        this.home = home;
        this.left = seconds;

        bar = new SimpleBar(Language.getMessage("command.faction.home.teleporting", player,
                new Parseable("{seconds}", "" + seconds)), BarColor.GREEN, 0, seconds);
        bar.addPlayer(player);
        HomeExtension.TELEPORTING_PLAYERS.put(player.getUniqueId(), this);

        runTaskTimer(MainIF.getIF(), 20, 20);

    }

    public static void teleport(Player player, Location home) {
        player.teleport(home);
        Language.sendMessage("command.faction.home.teleported", player);
    }

    @Override
    public void run() {
        left--;
        bar.setTitle(Language.getMessage("command.faction.home.teleporting", player, new Parseable("{seconds}", "" + left)));
        bar.setValue(left);

        if (left <= 0) {
            teleport(player, home);
            cancel();
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        bar.dispose();
        HomeExtension.TELEPORTING_PLAYERS.remove(player.getUniqueId());
    }
}
