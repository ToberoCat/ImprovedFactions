package io.github.toberocat.improvedfactions.factions.power;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PowerManager {

    private Faction faction;
    private int power;
    private int maxPower;

    private boolean isGeneratingPower;

    public PowerManager(Faction faction) {
        this.faction = faction;
        this.power = ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.startClaimPower");
        this.maxPower = power;
        isGeneratingPower = false;
    }

    public void addFactionMember() {
        maxPower += ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.powerPerPlayer");
        power += ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.powerPerPlayer");

        //ImprovedFactionsMain.getPlugin().getConfig().getInt("faction.powerPerPlayer");
    }

    public void removeFactionMember() {
        maxPower -= ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.powerPerPlayer");
        power -= Math.max(ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.minPower"),
                ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.powerPerPlayer"));
    }

    public void claimChunk() {
    }

    public void unclaimChunk() {
    }

    public boolean canClaimChunk() {
        return (power > faction.getClaimedChunks());
    }

    public void playerDeath(Player player) {
        if (!ImprovedFactionsMain.getPlugin().getConfig().getStringList("general.worlds")
                .contains(player.getWorld().getName())) return;

        power -= Math.max(ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.minPower"),
                ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.powerLossPerDeath"));

        for (FactionMember member : faction.getMembers()) {
            if (member == null) continue;
            OfflinePlayer off = Bukkit.getOfflinePlayer(member.getUuid());

            if (off.isOnline()) {
                off.getPlayer().sendMessage(Language.getPrefix() + Language.format(
                        "&eSomeone died in your faction. You current power is &b" + power));
                if (power < faction.getClaimedChunks()) {
                    off.getPlayer().sendMessage(Language.getPrefix() + Language.format(
                            "&e" + (power - faction.getClaimedChunks()) + "&e chunks are currently unprotected."));
                }
            }
        }

        startRegenerationThread();
    }

    public void startRegenerationThread() {
        if (isGeneratingPower) return;
        if (power == maxPower) return;
        new Thread(() -> {
            isGeneratingPower = true;
            Debugger.LogInfo("Started generation of power");
            while (power < maxPower) {
                try {
                    Thread.sleep(ImprovedFactionsMain.getPlugin().getConfig()
                            .getInt("factions.regenerationRate"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                regenerate();
            }

            isGeneratingPower = false;
        }).start();
    }

    private void regenerate() {
        List<Player> members = FactionUtils.getPlayersOnline(faction);
        power += members.size() *
                ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.regenerationPerRate");

        if (power >= maxPower) {
            power = maxPower;
        }
        for (Player player : members) {
            player.sendMessage(Language.getPrefix() + Language.format(
                    "&eSome power regenerated. Current power: &b" + power + " / " + maxPower));
        }
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }
}
