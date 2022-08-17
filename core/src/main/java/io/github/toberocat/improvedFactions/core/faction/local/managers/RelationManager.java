package io.github.toberocat.improvedFactions.core.faction.local.managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.improvedFactions.core.faction.Faction;

import java.util.ArrayList;

//ToDo: Rewrite required
public class RelationManager {
    protected final ArrayList<String> allyInvitations;
    private ArrayList<String> enemies;
    private ArrayList<String> allies;
    private Faction<?> faction;

    public RelationManager() {
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        allyInvitations = new ArrayList<>();
    }

    public RelationManager(Faction<?> faction) {
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        allyInvitations = new ArrayList<>();

        this.faction = faction;
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
    public Faction<?> getFaction() {
        return faction;
    }

    @JsonIgnore
    public void setFaction(Faction<?> faction) {
        this.faction = faction;
    }

    @JsonIgnore
    public ArrayList<String> getAllyInvitations() {
        return allyInvitations;
    }
}
