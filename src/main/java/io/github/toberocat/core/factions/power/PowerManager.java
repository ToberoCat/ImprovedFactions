package io.github.toberocat.core.factions.power;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.bossbar.AnimatedBossBar;
import io.github.toberocat.core.utility.bossbar.eases.EaseInOutSine;
import org.bukkit.boss.BarColor;

public class PowerManager {
    private int currentPower;
    private int maxPower;

    private Faction faction;

    private AnimatedBossBar bossBar;

    /**
     * Don't use this. It's for jackson (json).
     */
    public PowerManager() {
        this.bossBar = new AnimatedBossBar("&bPower " + 0 + "/" + 0, BarColor.BLUE, 0, 0, new EaseInOutSine());
    }

    public PowerManager(Faction faction, int maxPower) {
        this.maxPower = maxPower;
        this.currentPower = maxPower;
        this.faction = faction;

        this.bossBar = new AnimatedBossBar("&bPower " + currentPower + "/" + maxPower, BarColor.BLUE, 0, maxPower, new EaseInOutSine());
        bossBar.setValue(currentPower);
    }

    public void increaseMax(int amount) {
        maxPower += amount;
        currentPower = Math.min(currentPower + amount, maxPower);
        bossBar.setMax(maxPower);
        bossBar.setTitle("&bPower " + currentPower + "/" + maxPower);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());
    }

    public void addClaimedChunk() {
        faction.setClaimedChunks(faction.getClaimedChunks() + 1);
    }

    public void removeClaimedChunk() {
        faction.setClaimedChunks(faction.getClaimedChunks() - 1);
    }

    public boolean overclaimable() {
        return faction.getClaimedChunks() >= currentPower;
    }

    public void addFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        currentPower += Math.min(currentPower, maxPower);

        bossBar.setTitle("&bPower " + currentPower + "/" + maxPower);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());
    }

    public void removeFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        currentPower -= Math.min(currentPower, maxPower);

        bossBar.setTitle("&bPower " + currentPower + "/" + maxPower);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());

    }

    public int getCurrentPower() {
        return currentPower;
    }

    public void setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
        if (currentPower > maxPower) maxPower = currentPower;

        if (faction == null) return;
        bossBar.setTitle("&bPower " + currentPower + "/" + maxPower);
        bossBar.fade(currentPower, faction.getFactionMemberManager().getOnlinePlayers());
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
        if (maxPower < currentPower) currentPower = maxPower;

        if (faction == null) return;
        bossBar.setMax(maxPower);
        bossBar.setTitle("&bPower " + currentPower + "/" + maxPower);
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
