package io.github.toberocat.core.factions.bank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class FactionBank {

    @JsonIgnore
    public boolean hasBank;
    @JsonIgnore
    private Faction faction;
    @JsonIgnore
    private Economy economy;

    public FactionBank() {
    }

    public FactionBank(Faction faction) {
        this.faction = faction;
        economy = MainIF.getEconomy();
        if (economy != null) {
            economy.createBank(faction.getRegistryName(),
                    Bukkit.getOfflinePlayer(faction.getOwner()));
            hasBank = true;
        }
    }

    //<editor-fold desc="Deposit">
    @JsonIgnore
    public EconomyResponse deposit(OfflinePlayer player, double amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");

        EconomyResponse response = economy.withdrawPlayer(player, amount);

        if (response.transactionSuccess()) return deposit(amount);
        return response;
    }

    @JsonIgnore
    public EconomyResponse deposit(double amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankDeposit(faction.getRegistryName(), amount);
    }
    //</editor-fold>

    //<editor-fold desc="Withdraw">
    @JsonIgnore
    public EconomyResponse withdraw(OfflinePlayer player, double amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");

        EconomyResponse response = economy.bankWithdraw(faction.getRegistryName(), amount);
        if (response.transactionSuccess()) {
            economy.depositPlayer(player, amount);
            return response;
        }

        return response;
    }

    @JsonIgnore
    public EconomyResponse withdraw(double amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankWithdraw(faction.getRegistryName(), amount);
    }
    //</editor-fold>

    @JsonIgnore
    public EconomyResponse has(double amount) {
        if (!hasBank) return null;
        if (amount < 0) throw new IllegalArgumentException("Only positive numbers are allowed");
        return economy.bankHas(faction.getRegistryName(), amount);
    }

    @JsonIgnore
    public EconomyResponse balance() {
        if (!hasBank) return null;
        return economy.bankBalance(faction.getRegistryName());
    }

    @JsonIgnore
    public EconomyResponse delete() {
        if (!hasBank) return null;
        EconomyResponse response = economy.deleteBank(faction.getRegistryName());
        hasBank = !response.transactionSuccess();
        return response;
    }

    @JsonIgnore
    public Faction getFaction() {
        return faction;
    }

    @JsonIgnore
    public void setFaction(Faction faction) {
        this.faction = faction;
    }
}
