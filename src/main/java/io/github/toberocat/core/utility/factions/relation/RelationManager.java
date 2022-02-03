package io.github.toberocat.core.utility.factions.relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.core.utility.factions.Faction;

import java.util.ArrayList;

public class RelationManager {
    private ArrayList<String> enemies;
    private ArrayList<String> allies;
    private Faction faction;

    public RelationManager() {}

    public RelationManager(Faction faction) {
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        this.faction = faction;
    }

    public void MakeAlly(Faction faction) {
        String registry = faction.getRegistryName();
        if (allies.contains(registry)) return;

        enemies.remove(registry);
        allies.add(registry);
        faction.getRelationManager().MakeAlly(this.faction);
    }

    public void MakeEnemy(Faction faction) {
        String registry = faction.getRegistryName();
        if (enemies.contains(registry)) return;

        allies.remove(registry);
        enemies.add(registry);
        faction.getRelationManager().MakeEnemy(this.faction);
    }

    public void RemoveRelation(Faction faction) {
        String registry = faction.getRegistryName();
        if (!allies.contains(registry) && !enemies.contains(registry)) return;

        allies.remove(faction.getRegistryName());
        enemies.remove(faction.getRegistryName());
        faction.getRelationManager().RemoveRelation(this.faction);
    }

    public ArrayList<String> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<String> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<String> getAllies() {
        return allies;
    }

    public void setAllies(ArrayList<String> allies) {
        this.allies = allies;
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
