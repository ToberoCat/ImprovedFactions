package io.github.toberocat.improvedfactions.factions.relation;

import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.OwnerRank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RelationManager {
    private Faction faction;

    private ArrayList<String> allies;
    private ArrayList<String> enemies;
    private ArrayList<String> invites;

    public RelationManager(Faction faction) {
        this.faction = faction;
        this.allies =  new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.invites = new ArrayList<>();
    }

    public void sendInvite(Faction faction) {
        invites.add(faction.getRegistryName());
        new Thread(() -> {
            for (FactionMember member : FactionUtils.getMembersOnline(this.faction)) {
                if (member.getRank().isAdmin()) {
                    Bukkit.getPlayer(member.getUuid()).sendMessage(Language.getPrefix() +
                            Language.format("&e" + faction.getDisplayName() + " &f wants to be ally. Use &7/f allyaccept&f to accept, &7/f allycancel&f to deny"));
                }
            }
        }).start();
    }

    public void removeInvite(Faction faction) {
        invites.remove(faction.getRegistryName());
        new Thread(() -> {
            for (FactionMember member : FactionUtils.getMembersOnline(this.faction)) {
                if (member.getRank().isAdmin()) {
                    Bukkit.getPlayer(member.getUuid()).sendMessage(Language.getPrefix() +
                            Language.format("&e" + faction.getDisplayName() + " &frejected your invite to be allies"));
                }
            }
        }).start();
    }

    public void neutral(Faction faction) {
        allies.remove(faction.getRegistryName());
        enemies.remove(faction.getRegistryName());
        new Thread(() -> {
            for (Player member : FactionUtils.getPlayersOnline(this.faction)) {
                member.sendMessage(Language.getPrefix() + Language.format("&e" +
                        faction.getDisplayName() + " &f is now neutral towards your faction"));
            }
        }).start();
    }

    public void beginWar(Faction faction) {
        enemies.add(faction.getRegistryName());
        new Thread(() -> {
            for (Player member : FactionUtils.getPlayersOnline(this.faction)) {
                member.sendMessage(Language.getPrefix() + Language.format("&e" +
                        faction.getDisplayName() + " &f began a war with your faction"));
            }
        }).start();
    }

    public void makeAllies(Faction faction) {
        allies.add(faction.getRegistryName());
        new Thread(() -> {
            for (Player member : FactionUtils.getPlayersOnline(this.faction)) {
                member.sendMessage(Language.getPrefix() + Language.format("&e" +
                        faction.getDisplayName() + " &f is now your ally"));
            }
        }).start();
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public ArrayList<String> getAllies() {
        return allies;
    }

    public void setAllies(ArrayList<String> allies) {
        this.allies = allies;
    }

    public ArrayList<String> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<String> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }

    public void setInvites(ArrayList<String> invites) {
        this.invites = invites;
    }
}
