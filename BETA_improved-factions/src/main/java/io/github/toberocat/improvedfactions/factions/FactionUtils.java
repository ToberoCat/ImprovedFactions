package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class FactionUtils {

    public static Faction getFactionByRegistry(String _name) {
        String name = ChatColor.stripColor(_name);
        Faction fac = null;
        for (Faction faction : Faction.getFACTIONS()) {
            if (faction.getRegistryName().equals(name)) {
                fac = faction;
                break;
            }
        }
        return fac;
    }

    public static boolean areMembersOnline(Faction faction) {
        for (FactionMember member : faction.getMembers()) {
            if (Bukkit.getOfflinePlayer(member.getUuid()).isOnline()) return true;
        }
        return false;
    }

    public static List<FactionMember> getMembersOnline(Faction faction) {
        LinkedList<FactionMember> members = new LinkedList<>();
        for (FactionMember member : faction.getMembers()) {
            if (member == null) continue;

            OfflinePlayer off = Bukkit.getOfflinePlayer(member.getUuid());
            if (off.isOnline()) {
                members.add(member);
            }
        }
        return members;
    }

    public static List<Player> getPlayersOnline(Faction faction) {
        LinkedList<Player> members = new LinkedList<Player>();
        for (FactionMember member : faction.getMembers()) {
            if (member == null) continue;

            OfflinePlayer off = Bukkit.getOfflinePlayer(member.getUuid());
            if (off.isOnline()) {
                members.add(off.getPlayer());
            }
        }
        return members;
    }

    public static List<Player> getAllPlayers(Faction faction) {
        LinkedList<Player> members = new LinkedList<>();
        for (FactionMember member : faction.getMembers()) {
            if (member == null) continue;

            OfflinePlayer off = Bukkit.getOfflinePlayer(member.getUuid());
            members.add(off.getPlayer());
        }
        return members;
    }

    public static List<FactionMember> getAllFactionMembers(Faction faction) {
        LinkedList<FactionMember> members = new LinkedList<>();
        for (FactionMember member : faction.getMembers()) {
            if (member == null) continue;

            members.add(member);
        }
        return members;
    }



    public static boolean shouldChunk(Player player, Faction playerFaction, Faction protectionFaction, Chunk chunk) {
        return true;
    }

    public static Faction getFaction(Player player) {
        for (Faction faction : Faction.getFACTIONS()) {
            for (FactionMember factionMember : faction.getMembers()) {
                if (factionMember != null && factionMember.getUuid().equals(player.getUniqueId())) {
                    return faction;
                }
            }
        }
        return null;
    }

    public static Faction getFaction(UUID player) {
        for (Faction faction : Faction.getFACTIONS()) {
            for (FactionMember factionMember : faction.getMembers()) {
                if (factionMember != null && factionMember.getUuid().equals(player)) {
                    return faction;
                }
            }
        }
        return null;
    }


    public static Rank getPlayerRank(Faction faction, Player player) {
        for (FactionMember factionMember : faction.getMembers()) {
            if (factionMember != null && factionMember.getUuid().equals(player.getUniqueId())) {
                return factionMember.getRank();
            }
        }
        return null;
    }
}
