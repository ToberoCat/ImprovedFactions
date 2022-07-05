package io.github.toberocat.improvedfactions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.improvedfactions.events.FullCacheCreatedEvent;
import io.github.toberocat.improvedfactions.listener.PlayerListener;
import io.github.toberocat.improvedfactions.mesh.MeshCache;
import io.github.toberocat.improvedfactions.particle.ParticleHandler;
import io.github.toberocat.improvedfactions.particle.ParticlePerformance;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class ClaimParticleExtension extends Extension implements Listener {
    public static final String PERFORMANCE_SETTING = "particle_performance";
    public static int VISUALISATION_INTERVAL;
    public static boolean DISPLAY_UNCLAIMABLE;
    public static int MAX_PARTICLE_RENDER_DISTANCE;
    public static int PARTICLE_HEIGHT;
    public static int PARTICLE_HIDE_DST;

    public static Particle PARTICLE;
    public static ParticleHandler handler;

    @Override
    protected void onEnable(MainIF plugin) {
        reloadConfigs();
        addPlayerSettings();
    }

    @Override
    protected void onDisable(MainIF plugin) {
        if (handler != null) handler.dispose();
    }

    @Override
    public void reloadConfigs() {
        setConfigDefaultValue("interval", 25L);
        VISUALISATION_INTERVAL = configValue("interval");

        setConfigDefaultValue("display-unclaimable", true);
        DISPLAY_UNCLAIMABLE = configValue("display-unclaimable");

        setConfigDefaultValue("max-particle-render-distance", Bukkit.getViewDistance());
        MAX_PARTICLE_RENDER_DISTANCE = configValue("max-particle-render-distance");

        setConfigDefaultValue("particle-height", 1);
        PARTICLE_HEIGHT = configValue("particle-height");

        setConfigDefaultValue("hide-particle-dst", 10);
        PARTICLE_HIDE_DST = configValue("hide-particle-dst");

        setConfigDefaultValue("particle-type", "REDSTONE");
        PARTICLE = Particle.valueOf(configValue("particle-type"));
    }

    @EventHandler
    private void fullCache(FullCacheCreatedEvent event) {
        registerListeners(MainIF.getIF());
        createShapes(MainIF.getIF());
        addPlayers();
    }

    private void addPlayerSettings() {
        EnumSetting setting = new EnumSetting(ParticlePerformance.values(), PERFORMANCE_SETTING,
                Utility.createItem(Material.SUGAR, "&eParticle performance"));
        setting.setUpdate((player, selected) -> handler.update(player, selected));
        PlayerSettings.add(setting);
    }

    private void registerListeners(MainIF plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

    private void createShapes(MainIF plugin) {
        handler = new ParticleHandler(plugin);
    }

    private void addPlayers() {
        for (Player online : Bukkit.getOnlinePlayers()) handler.addPlayer(online);
    }
}
