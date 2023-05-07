/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedfactions.spigot.handler;

import io.github.toberocat.improvedFactions.core.handler.SoundHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpigotSoundHandler implements SoundHandler {
    @Override
    public void playSound(@NotNull FactionPlayer player, @NotNull String sound, float volume, float pitch) {
        Player spigot = (Player) player.getRaw();
        spigot.playSound(spigot.getLocation(), sound, volume, pitch);
    }
}
