package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.event.faction.FactionJoinEvent;
import io.github.toberocat.improvedfactions.event.faction.FactionLeaveEvent;
import io.github.toberocat.improvedfactions.factions.economy.Bank;
import io.github.toberocat.improvedfactions.factions.power.PowerManager;
import io.github.toberocat.improvedfactions.factions.relation.RelationManager;
import io.github.toberocat.improvedfactions.ranks.AllyRank;
import io.github.toberocat.improvedfactions.ranks.GuestRank;
import io.github.toberocat.improvedfactions.ranks.OwnerRank;
import io.github.toberocat.improvedfactions.utility.*;
import io.github.toberocat.improvedfactions.utility.Utils;
import io.github.toberocat.improvedfactions.utility.configs.DataManager;
import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.gui.Flag;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.Rank;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Faction {
    private static List<Faction> FACTIONS = new ArrayList<Faction>();

    public static List<FactionData> data = new ArrayList<>();

    public enum OpenType {
        Public("&aPublic"),
        Private("&cPrivate");

        private final String display;
        OpenType(String display) {
            this.display = Language.format(display);
        }

        @Override
        public String toString() {
            return display;
        }
    }

    public static final String CLAIM_CHUNK_PERMISSION = "claim_chunk";
    public static final String UNCLAIM_CHUNK_PERMISSION = "unclaim_chunk";
    public static final String INVITE_PERMISSION = "invite";
    public static final String BUILD_PERMISSION = "build";
    public static final String BREAK_PERMISSION = "break";
    public static final String LIST_BANNED_PERMISSION = "listBanned";

    public static final String OPENTYPE_FLAG = "openType";
    public static final String RENAME_FLAG = "rename";
    public static final String MOTD = "motd";

    private String rules;

    private String displayName;
    private String registryName;
    private FactionMember[] members;

    private String description = "A improved faction faction";
    private String motd = "";

    private PowerManager powerManager;
    private RelationManager relationManager;
    private Bank bank;

    private boolean permanent;
    private boolean frozen;

    private int claimChunks;

    private final FactionSettings settings;
    private List<UUID> bannedPeople;

    private UUID owner;

    public static Faction create(Player owner, String displayName) {
        Faction faction = new Faction(owner, displayName);
        return faction;
    }

    private Faction(OfflinePlayer owner, String displayName) {
        this.displayName = displayName;
        this.registryName = ChatColor.stripColor(displayName);
        this.claimChunks = 0;
        this.owner = owner.getUniqueId();

        // ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.maxMembers")
        members = new FactionMember[ImprovedFactionsMain.getPlugin().getConfig().getInt("factions.maxMembers")];


        bannedPeople = new ArrayList<>();
        settings = new FactionSettings();


        relationManager = new RelationManager(this);
        powerManager = new PowerManager(this);
        bank = new Bank(this);

        permanent = ImprovedFactionsMain.getPlugin().getConfig().getBoolean("faction.permanent");
        frozen = false;

        FACTIONS.add(this);
    }

    public String getDisplayName() {
        return displayName;
    }

    public FactionRankPermission getPermission(String perm) {
        return settings.getRanks().get(perm);
    }

    /**
     * This function checks if the player has the permission
     *
     * @param player The player who is trying to use the command
     * @param permission The permission you want to check.
     * @return A boolean.
     */
    public boolean hasPermission(Player player, String permission) {
        FactionMember member = getFactionMember(player);
        if (member == null) {
            for (String ally : relationManager.getAllies()) {
                Faction allied = FactionUtils.getFactionByRegistry(ally);
                FactionMember[] players = FactionUtils.getAllFactionMembers(allied).stream().filter(x -> x.getUuid() == player.getUniqueId()).toArray(FactionMember[]::new);
                if (players.length == 1) {
                    member = new FactionMember(player.getUniqueId(), Rank.fromString(AllyRank.registry));
                    break;
                }
            }

            if (member == null) {
                member = new FactionMember(player.getUniqueId(), Rank.fromString(GuestRank.registry));
            }
        }

        FactionRankPermission perms = settings.getRanks().get(permission);
        if (perms == null) {
            ImprovedFactionsMain.getConsoleSender().sendMessage("Couldn't get permissions for "
                    + permission + ". This should not happen. If it does (It did if you are reading this), please report it to me (The developer). Send me a message over discord or spigotmc. Go on spigotmc to get a link to discord");
            return false;
        }
        return perms.getRanks().contains(member.getRank());
    }

    public FactionMember getFactionMember(Player player) {
        for (FactionMember factionMember : members) {
            if (factionMember != null && factionMember.getUuid().equals(player.getUniqueId())) {
                return factionMember;
            }
        }

        return null;
    }

    public boolean hasMaxMembers() {
        for (FactionMember member : members) {
            if (member == null) return false;
        }
        return true;
    }

    public void SetRank(Player player, Rank rank) {
        for (FactionMember factionMember : members) {
            if (factionMember != null && factionMember.getUuid().equals(player.getUniqueId())) {
                factionMember.setRank(rank);
            }
        }
    }

    public boolean Join(Player player, Rank rank) {
        boolean result = JoinSilent(player, rank);
        if (!result) return false;
        FactionJoinEvent joinEvent = new FactionJoinEvent(this, player);
        Bukkit.getPluginManager().callEvent(joinEvent);

        return true;
    }

    public boolean JoinSilent(Player player, Rank rank) {
        if (frozen) return false;
        if (bannedPeople.contains(player.getUniqueId())) return false;
        boolean success = false;
        for (int i = 0; i < members.length; i++) {
            if (members[i] == null) {
                members[i] = new FactionMember(player.getUniqueId(), rank);
                success = true;
                break;
            }
        }
        if (!success) return false;

        ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction = this;

        powerManager.addFactionMember();

        return true;
    }

    public boolean Join(UUID uuid, Rank rank) {
        if (frozen) return false;
        if (bannedPeople.contains(uuid)) return false;
        boolean success = false;
        for (int i = 0; i < members.length; i++) {
            if (members[i] == null) {
                members[i] = new FactionMember(uuid, rank);
                success = true;
                break;
            }
        }
        if (!success) return false;

        ImprovedFactionsMain.playerData.get(uuid).playerFaction = this;

        powerManager.addFactionMember();

        return true;
    }

    public void ClaimChunk(Chunk chunk, TCallback<ClaimStatus> callback) {
        if (!powerManager.canClaimChunk()) {
            callback.Callback(null);
            return;
        }
        ChunkUtils.ClaimChunk(chunk, this, result -> {
            if (result.getClaimStatus() == ClaimStatus.Status.SUCCESS) {
                powerManager.claimChunk();
                claimChunks++;
            }
            callback.Callback(result);
        });
    }

    public void UnClaimChunk(Chunk chunk, TCallback<ClaimStatus> callback) {
        ChunkUtils.UnClaimChunk(chunk, this, result -> {
            if (result.getClaimStatus() == ClaimStatus.Status.SUCCESS) {
                powerManager.unclaimChunk();
                claimChunks--;
            };
            callback.Callback(result);
        });
    }

    public boolean DeleteFaction() {
        if (frozen) return false;

        for (FactionMember member : members) {
            if (member != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(member.getUuid());
                ImprovedFactionsMain.getPlugin().getPlayerMessages().SendMessage(player,Language.getPrefix() + displayName + " got deleted. You left automatically");
            }
        }
        FACTIONS.remove(this);

        bank.delete();

        for (String ally : relationManager.getAllies()) {
            FactionUtils.getFactionByRegistry(ally).relationManager.neutral(this);
        }

        for (String enemies : relationManager.getEnemies()) {
            FactionUtils.getFactionByRegistry(enemies).relationManager.neutral(this);
        }
        return true;
    }


    public boolean Leave(Player player) {
        if (frozen) return false;

        for (int i = 0; i < members.length; i++) {
            if (members[i] != null && members[i].getUuid().equals(player.getUniqueId())) {
                members[i] = null;
                return true;
            }
        }
        ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction = null;
        powerManager.removeFactionMember();
        return false;
    }

    public boolean Leave(OfflinePlayer player) {
        if (frozen) return false;

        for (int i = 0; i < members.length; i++) {
            if (members[i] != null && members[i].getUuid().equals(player.getUniqueId())) {
                members[i] = null;
                return true;
            }
        }
        FactionLeaveEvent leaveEvent = new FactionLeaveEvent(this, player);
        Bukkit.getPluginManager().callEvent(leaveEvent);

        powerManager.removeFactionMember();
        ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction = null;
        return false;
    }

    public static void LoadFactions(ImprovedFactionsMain plugin) {
        DataManager data = plugin.getFactionData();
        if (data.getConfig().getConfigurationSection("f") == null) return;
        for (String key : data.getConfig().getConfigurationSection("f").getKeys(false)) {
            String displayName = ChatColor.translateAlternateColorCodes('&', data.getConfig().getString("f." +key + ".displayName"));

            FactionMember[] members = new FactionMember[plugin.getConfig().getInt("factions.maxMembers")];
            Arrays.fill(members, null);

            List<String> raw = data.getConfig().getStringList("f." +key + ".members");
            for (int i = 0; i < raw.size(); i++) {
                String rawMember = raw.get(i);
                members[i] = FactionMember.fromString(rawMember);
            }

            List<String> rawFlags = data.getConfig().getStringList("f." +key + ".settings.flags");
            List<Flag> flags = new ArrayList<>();
            for (String str : rawFlags) {
                flags.add(Flag.fromString(str));
            }

            String owner = data.getConfig().getString("f." + key + ".owner");
            if (owner == null) {
                for (FactionMember member : members) {
                    if (member.getRank().getRegistryName().equals(OwnerRank.registry)) {
                        owner = member.getUuid().toString();
                    }
                }
            }

            UUID uuid = UUID.fromString(owner);
            Faction faction = new Faction(Bukkit.getOfflinePlayer(uuid), displayName);
            faction.members = members;
            for (int i = 0; i < flags.size(); i++) {
                faction.settings.getFlags().put(rawFlags.get(i).split("::")[0], flags.get(i));
            }

            List<String> rawBanned = data.getConfig().getStringList("f." +key + ".banned");
            List<UUID> banned = new ArrayList<>();

            for (String rawBan : rawBanned) {
                try{
                    rawBan = rawBan.replace("]", "").replace("[", "");
                    banned.add(UUID.fromString(rawBan.trim()));
                } catch (IllegalArgumentException exception){
                    Debugger.LogWarning("&cCouldn't load banned");
                }
            }

            faction.powerManager = new PowerManager(faction);
            faction.powerManager.setPower(data.getConfig().getInt("f." +key + ".power"));
            faction.powerManager.setMaxPower(data.getConfig().getInt("f." +key + ".maxPower"));
            faction.powerManager.startRegenerationThread();

            faction.relationManager = new RelationManager(faction);
            faction.relationManager.setAllies((ArrayList<String>)
                    data.getConfig().getStringList("f." + key + ".allies"));
            faction.relationManager.setEnemies((ArrayList<String>)
                    data.getConfig().getStringList("f." + key + ".enemies"));
            faction.relationManager.setInvites((ArrayList<String>)
                    data.getConfig().getStringList("f." + key + ".invites"));


            if (data.getConfig().contains("f." + key + ".permanent")) {
                faction.permanent = data.getConfig().getBoolean("f." + key + ".permanent");
            }
            if (data.getConfig().contains("f." + key + ".frozen")) {
                faction.frozen = data.getConfig().getBoolean("f." + key + ".frozen");
            }

            faction.claimChunks = data.getConfig().getInt("f." +key + ".claimedChunks");
            faction.bannedPeople = banned;
            faction.setMotd(data.getConfig().getString("f." +key + ".motd"));
            faction.setDescription(data.getConfig().getString("f." +key + ".description"));
            for (String perm : data.getConfig().getStringList("f." +key + ".settings.permissions")) {
                String[] parms = perm.split("::");
                faction.settings.getRanks().put(parms[0], FactionRankPermission.fromString(perm));
            }


            for (FactionData dat : Faction.data) {
                dat.Load(faction, data);
            }
        }
    }

    public static void SaveFactions(ImprovedFactionsMain plugin) {
        DataManager data = plugin.getFactionData();
        data.getConfig().set("f", null);
        data.saveConfig();
        for (Faction faction : FACTIONS) {
            List<String> _members = new ArrayList<String>();
            for (FactionMember factionMember : faction.getMembers()) {
                if (factionMember != null)
                    _members.add(factionMember.toString());
            }

            List<String> _permissions = new ArrayList<>();
            for (String key : faction.settings.getRanks().keySet()) {
                FactionRankPermission permission = faction.getPermission(key);
                _permissions.add(key + "::" + permission.toString());
            }

            List<String> flags = new ArrayList<>();
            for (String key : faction.settings.getFlags().keySet()) {
                flags.add(key + "::" + faction.settings.getFlags().get(key).toString());
            }

            data.getConfig().set("f." + faction.getRegistryName() + ".allies", faction.relationManager.getAllies());
            data.getConfig().set("f." + faction.getRegistryName() + ".enemies", faction.relationManager.getEnemies());
            data.getConfig().set("f." + faction.getRegistryName() + ".invites", faction.relationManager.getInvites());

            data.getConfig().set("f." + faction.getRegistryName() + ".permanent", faction.permanent);
            data.getConfig().set("f." + faction.getRegistryName() + ".frozen", faction.frozen);


            data.getConfig().set("f." + faction.getRegistryName() + ".owner", faction.owner.toString());
            data.getConfig().set("f." + faction.getRegistryName() + ".claimedChunks", faction.claimChunks);
            data.getConfig().set("f." + faction.getRegistryName() + ".maxPower", faction.powerManager.getMaxPower());
            data.getConfig().set("f." + faction.getRegistryName() + ".displayName", faction.getDisplayName());
            data.getConfig().set("f." + faction.getRegistryName() + ".description", faction.getDescription());
            data.getConfig().set("f." + faction.getRegistryName() + ".motd", faction.getMotd());
            data.getConfig().set("f." + faction.getRegistryName() + ".power", faction.powerManager.getPower());
            data.getConfig().set("f." + faction.getRegistryName() + ".settings.flags", flags);
            data.getConfig().set("f." + faction.getRegistryName() + ".settings.permissions", _permissions);
            data.getConfig().set("f." + faction.getRegistryName() + ".members", _members.toArray());
            data.getConfig().set("f." + faction.getRegistryName() + ".banned", Utils.listToStringList(faction.bannedPeople));

            data.saveConfig();
            for (FactionData dat : Faction.data) {
                dat.Save(faction, data);
            }
        }
    }

    public String getRegistryName() {
        return registryName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = Language.format(displayName).replaceAll(" ", "");
    }

    public String getDescription() {
        return description;
    }

    public List<UUID> getBannedPeople() {
        return bannedPeople;
    }

    public void setBannedPeople(List<UUID> bannedPeople) {
        this.bannedPeople = bannedPeople;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public void setMembers(FactionMember[] members) {
        this.members = members;
    }

    public FactionSettings getSettings() {
        return settings;
    }

    public static List<Faction> getFACTIONS() {
        return FACTIONS;
    }

    public static void setFACTIONS(List<Faction> FACTIONS) {
        Faction.FACTIONS = FACTIONS;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public void setRegistryName(String registryName) {
        this.registryName = registryName;
    }

    public PowerManager getPowerManager() {
        return powerManager;
    }

    public void setPowerManager(PowerManager powerManager) {
        this.powerManager = powerManager;
    }

    public int getClaimedChunks() {
        return claimChunks;
    }

    public void setClaimedChunks(int claimChunks) {
        this.claimChunks = claimChunks;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public int getClaimChunks() {
        return claimChunks;
    }

    public void setClaimChunks(int claimChunks) {
        this.claimChunks = claimChunks;
    }

    public RelationManager getRelationManager() {
        return relationManager;
    }

    public void setRelationManager(RelationManager relationManager) {
        this.relationManager = relationManager;
    }

    public FactionMember[] getMembers() {
        return members;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
}

