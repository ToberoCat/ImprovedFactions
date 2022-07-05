package io.github.toberocat.improvedfactions.factions.economy;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.factions.Faction;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Bank {
    private Faction faction;
    private Economy economy;

    private boolean hasBank;

    public Bank(Faction faction) {
        this.faction = faction;
        hasBank = false;
        Bukkit.getScheduler().runTaskLater(ImprovedFactionsMain.getPlugin(), () -> {
            economy = ImprovedFactionsMain.getPlugin().getEconomy();
            if (economy != null) {
                ImprovedFactionsMain.getPlugin().getEconomy().createBank(faction.getRegistryName(),
                        Bukkit.getOfflinePlayer(faction.getOwner()));
                hasBank = true;
            }
        }, 1);
    }

    //<editor-fold desc="Deposit">
    public EconomyResponse deposit(OfflinePlayer player, int amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");

        EconomyResponse response = economy.withdrawPlayer(player, amount);

        if (response.transactionSuccess()) return deposit(amount);
        return response;
    }

    public EconomyResponse deposit(int amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankDeposit(faction.getRegistryName(), amount);
    }
    //</editor-fold>

    //<editor-fold desc="Withdraw">
    public EconomyResponse withdraw(OfflinePlayer player, int amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");

        EconomyResponse response = economy.bankWithdraw(faction.getRegistryName(), amount);
        if (response.transactionSuccess()) {
            economy.depositPlayer(player, amount);
            return response;
        }

        return response;
    }

    public EconomyResponse withdraw(int amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankWithdraw(faction.getRegistryName(), amount);
    }
    //</editor-fold>

    public EconomyResponse has(int amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankHas(faction.getRegistryName(), amount);
    }

    public EconomyResponse balance() {
        if (!hasBank) return null;
        return economy.bankBalance(faction.getRegistryName());
    }

    public EconomyResponse delete() {
        if (!hasBank) return null;
        EconomyResponse response = economy.deleteBank(faction.getRegistryName());
        hasBank = !response.transactionSuccess();
        return response;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
