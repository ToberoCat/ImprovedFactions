package io.github.toberocat.core.factions.local.managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.events.faction.FactionAllyEvent;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RelationManager {
    private ArrayList<String> enemies;
    private ArrayList<String> allies;
    protected ArrayList<String> allyInvitations;
    private Faction faction;

    public RelationManager() {
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        allyInvitations = new ArrayList<>();
    }

    public RelationManager(Faction faction) {
        enemies = new ArrayList<>();
        allies = new ArrayList<>();
        allyInvitations = new ArrayList<>();

        this.faction = faction;
    }

    public Result inviteAlly(Faction invited) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(invited.getOwner());
        if (!player.isOnline()) return Result.failure("OWNER_OFFLINE",
                "The faction owner is offline. Please wait until he's online");

        Player owner = player.getPlayer();
        if (owner == null) return Result.failure("", "");

        invited.getRelationManager().allyInvitations.add(faction.getRegistryName());

        new PlayerMessageBuilder("%;{CLICK(0)}%&e" + faction.getDisplayName() +
                "&f wants to be your &bally&f. &7Click to accept\n&fInvitation will run out in &e5 &fminutes",
                "/f relation allyAccept " + faction.getRegistryName())
                .send(owner);
        Bukkit.getScheduler().runTaskLater(MainIF.getIF(), () ->
                removeAllyInvite(faction), 5 * 60 * 20);

        return Result.success();
    }

    public void acceptInvite(Faction other) {
        allyInvitations.remove(other.getRegistryName());
        makeAlly(other);
    }

    public void removeAllyInvite(Faction invited) {
        invited.getRelationManager().allyInvitations.remove(faction.getRegistryName());

        Player owner = Bukkit.getOfflinePlayer(faction.getOwner()).getPlayer();
        if (owner == null) return;

        Language.sendRawMessage("Invitation for being allies &e" + faction.getDisplayName() +
                "&f has now run out of time", owner);

    }

    private void makeAlly(Faction faction) {
        String registry = faction.getRegistryName();
        if (allies.contains(registry)) return;
        if (faction.getRelationManager().allies.contains(this.faction.getRegistryName())) AsyncTask
                .runSync(() -> Bukkit.getPluginManager().callEvent(new FactionAllyEvent(faction, this.faction)));
        enemies.remove(registry);
        allies.add(registry);

        faction.getRelationManager().makeAlly(this.faction);
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

    @JsonIgnore
    public ArrayList<String> getAllyInvitations() {
        return allyInvitations;
    }
}
