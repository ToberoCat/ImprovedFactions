package io.github.toberocat.core.factions.power;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public class PowerManager {
    private int currentPower;
    private int maxPower;

    private Faction faction;

    private BossBar bossBar;

    /**
     * Don't use this. It's for jackson (json).
     */
    public PowerManager() {
    }

    public PowerManager(Faction faction, int maxPower) {
        this.maxPower = maxPower;
        this.currentPower = maxPower;
        this.faction = faction;

        this.bossBar = Bukkit.createBossBar(Language.format("&9Power"), BarColor.BLUE, BarStyle.SEGMENTED_10);
    }

    public void IncreaseMax(int amount) {
        maxPower += amount;
        currentPower = Math.min(currentPower + amount, maxPower);
    }

    public void addClaimedChunk() {

    }

    public void addFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        currentPower += Math.min(currentPower, maxPower);

        //ToDo: Implement bossbar animation for power rising
        if (MainIF.getConfigManager().getValue("power.bossbar")) {
            /*
            for (UUID playerUUID : faction.getOnlineMembers()) {
                bossBar.addPlayer(Bukkit.getPlayer(playerUUID));
            }
             */
        }
    }

    public void removeFactionMember() {
        int powerPerPlayer = MainIF.getConfigManager().getValue("power.powerPerPlayer");
        currentPower -= Math.min(currentPower, maxPower);

        //ToDo: Implement bossbar animation for power sinking
        if (MainIF.getConfigManager().getValue("power.bossbar")) {
            /*
            for (UUID playerUUID : faction.getOnlineMembers()) {
                bossBar.addPlayer(Bukkit.getPlayer(playerUUID));
            }
             */
        }
    }

    public int getCurrentPower() {
        return currentPower;
    }

    public PowerManager setCurrentPower(int currentPower) {
        this.currentPower = currentPower;
        return this;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public PowerManager setMaxPower(int maxPower) {
        this.maxPower = maxPower;
        return this;
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
