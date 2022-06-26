package io.github.toberocat.core.factions.power;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.bossbar.AnimatedBossBar;
import io.github.toberocat.core.utility.bossbar.eases.EaseInOutSine;
import io.github.toberocat.core.utility.events.faction.FactionPowerEvent;
import io.github.toberocat.core.utility.events.faction.power.PowerCause;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;

public class PowerManager {
    private int currentPower;
    private int maxPower;

    private Faction faction;


    /**
     * Don't use this. It's for jackson (json).
     */
    public PowerManager() {
    }

    public PowerManager(Faction faction, int maxPower) {
        this.maxPower = maxPower;
        this.currentPower = maxPower;
        this.faction = faction;
    }

    public void increaseMax(int amount) {
        int old = maxPower;
        maxPower += amount;
        currentPower = Math.min(currentPower + amount, maxPower);

        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());

        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new FactionPowerEvent(faction, amount, PowerCause.MAX_POWER)));
    }

    public void addClaimedChunk() {
        faction.setClaimedChunks(faction.getClaimedChunks() + 1);
    }

    public void death() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.memberDeathConsume");
        int old = currentPower;
        currentPower = Math.min(currentPower - powerPerPlayer, maxPower);

        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());

        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new FactionPowerEvent(faction, powerPerPlayer, PowerCause.PLAYER_KILLED)));
    }

    public void removeClaimedChunk() {
        faction.setClaimedChunks(faction.getClaimedChunks() - 1);
    }

    public boolean overclaimable() {
        return faction.getClaimedChunks() >= currentPower;
    }

    public void addFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        int old = currentPower;
        maxPower += currentPower;
        currentPower = Math.min(currentPower + powerPerPlayer, maxPower);

        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());

        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new FactionPowerEvent(faction, powerPerPlayer, PowerCause.PLAYER_JOIN)));
    }

    public void removeFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        int old = currentPower;
        maxPower -= currentPower;
        currentPower = Math.min(currentPower - powerPerPlayer, maxPower);

        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());

        AsyncTask.runSync(() -> Bukkit.getPluginManager()
                .callEvent(new FactionPowerEvent(faction, powerPerPlayer, PowerCause.PLAYER_LEFT)));
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        int old = this.currentPower;
        this.currentPower = currentPower;
        if (currentPower > maxPower) maxPower = currentPower;

        if (faction == null) return;
        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        int old = this.maxPower;
        this.maxPower = maxPower;
        if (maxPower < currentPower) currentPower = maxPower;

        if (faction == null) return;
        AnimatedBossBar bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower,
                BarColor.BLUE, 0, maxPower);
        bossBar.setValue(old);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());
    }

    @JsonIgnore
    public Faction getFaction() {
        return faction;
    }

    @JsonIgnore
    public PowerManager setFaction(Faction faction) {
        this.faction = faction;
        return this;
    }

}
