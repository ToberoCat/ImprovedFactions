package io.github.toberocat.core.factions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.bank.FactionBank;
import io.github.toberocat.core.factions.members.FactionMemberManager;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.power.PowerManager;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.factions.rank.members.OwnerRank;
import io.github.toberocat.core.factions.relation.RelationManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.Claim;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.events.faction.*;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.messages.MessageSystem;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Faction {

    private static final Map<String, Faction> LOADED_FACTIONS = new HashMap<>();
    private PowerManager powerManager;
    private FactionMemberManager factionMemberManager;
    private RelationManager relationManager;
    private FactionBank factionBank;
    private FactionPerm factionPerm;
    private String displayName, registryName, motd, tag;
    private String[] description;
    private boolean frozen, permanent;
    private String createdAt;
    private UUID owner;
    private int claimedChunks;

    /**
     * Don't use this. it's for jackson (json).
     */
    public Faction() {
    }

    private Faction(String displayName, String registryName, UUID owner, OpenType openType) {
        super();
        this.registryName = registryName;
        this.displayName = displayName;

        this.powerManager = new PowerManager(this,
                MainIF.getConfigManager().getValue("power.maxDefaultFaction"));
        this.factionMemberManager = new FactionMemberManager(this);
        this.relationManager = new RelationManager(this);
        this.factionBank = new FactionBank();
        this.factionPerm = new FactionPerm(this);

        this.frozen = false;
        this.permanent = Boolean.TRUE.equals(MainIF.getConfigManager().getValue("faction.permanent"));
        this.description = new String[]{"&eCool &cfaction"};
        this.motd = "New faction";
        this.tag = "IFF";
        this.claimedChunks = 0;
        this.owner = owner;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        this.createdAt = fmt.print(new LocalDateTime());
    }

    /**
     * It creates a new Faction object, and adds it to the LOADED_FACTIONS map
     *
     * @param displayName The name of the faction.
     * @param owner       The player who created the faction
     * @return A Result object.
     */
    public static Result<Faction> createFaction(String displayName, Player owner) {
        if (displayName.length() >= (Integer) MainIF.getConfigManager().getValue("faction.maxNameLen"))
            return Result.failure("OVER_MAX_LEN", "You reached the maximum length for a faction name");
        String registryName = ChatColor.stripColor(displayName.replaceAll("[^a-zA-Z0-9]", ""));

        if (MainIF.getConfigManager().getValue("forbidden.checkFactionNames")) {
            Result result = AsyncTask.run(() -> {
                ArrayList<String> forbiddenNames =
                        MainIF.getConfigManager().getValue("forbidden.factionNames");

                String checkRegistry = MainIF.getConfigManager().getValue("forbidden.checkLeetspeak") ?
                        Language.simpleLeetToEnglish(registryName) : registryName;

                if (forbiddenNames.contains(checkRegistry))
                    return new Result(false).setMessages("FORBIDDEN_NAME",
                            "Sorry, but this name is forbidden");

                double disbandPercent = MainIF.getConfigManager().getValue("forbidden.disbandAtPercent");
                double reportPercent = MainIF.getConfigManager().getValue("forbidden.reportAtPercent");

                disbandPercent /= 100;
                reportPercent /= 100;

                for (String forbidden : forbiddenNames) {
                    double prediction = Language.similarity(forbidden, checkRegistry);

                    if (prediction > disbandPercent) {
                        return new Result(false).setMessages("FORBIDDEN_NAME",
                                "Sorry, but this name is forbidden");
                    } else if (prediction > reportPercent) {
                        ArrayList<String> reportCommands = MainIF.getConfigManager().getValue("commands.forbidden");

                        MainIF.getIF().getServer().getScheduler().runTaskLater(MainIF.getIF(), () -> {
                            for (String command : reportCommands) {
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
                                        Language.parse(command, new Parseable[]{
                                                new Parseable("{word}", registryName),
                                                new Parseable("{similar}", forbidden),
                                                new Parseable("{player_name}", owner.getName()),
                                                new Parseable("{player_uuid}", owner.getUniqueId().toString()),
                                                new Parseable("{task}", "FACTION_CREATION"),
                                                new Parseable("{similarityPer}", prediction * 100 + "")
                                        }));
                            }
                        }, 0);
                        return new Result(false).setMessages("MAYBE_FORBIDDEN",
                                "Your faction name is very similar to a forbidden word. If you think your name is fine, just ignore it. If you want to retreat the report, just change the name to something appropriate");
                    }
                }

                return new Result(true);
            }).await();

            if (!result.isSuccess()) return result;
        }
        Faction newFaction = new Faction(displayName, registryName, owner.getUniqueId(), OpenType.PUBLIC);
        boolean canCreate = Utility.callEvent(new FactionCreateEvent(newFaction, owner));
        if (!canCreate) {
            DataAccess.removeFile("Factions", registryName);

            return new Result<Faction>(false).setMessages("EVENT_CANCEL",
                    "Couldn't create your faction");
        }

        LOADED_FACTIONS.put(registryName, newFaction);
        Result result = newFaction.join(owner, Rank.fromString(OwnerRank.registry));

        if (!result.isSuccess()) {
            newFaction.delete();
            return result;
        }

        DataAccess.addFile("Factions", registryName, newFaction);
        return result.setPaired(newFaction);
    }

    //<editor-fold desc="Getters and Setters">
    public static Map<String, Faction> getLoadedFactions() {
        return LOADED_FACTIONS;
    }

    public Rank getPlayerRank(Player player) {
        return factionPerm.getPlayerRank(player);
    }

    public boolean hasPermission(Player player, String permission) {
        Validate.notNull(player);
        Validate.notNull(permission);

        Rank rank = getPlayerRank(player);
        if (rank == null) {
            Debugger.logWarning("Wasn't able to get rank for " + player.getName());
            return false;
        }
        return factionPerm.getRankSetting().get(permission).hasPermission(rank);
    }

    /**
     * Let a player join a faction
     *
     * @param player
     * @param rank
     * @return
     */
    public Result join(Player player, Rank rank) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't join");

        boolean canJoin = Utility.callEvent(new FactionJoinEvent(this, player));
        if (!canJoin) return Result.failure("EVENT_CANCEL",
                "Couldn't join faction");

        return factionMemberManager.join(player);
    }

    /**
     * Let a player leave the faction
     *
     * @param player The player who is leaving the faction
     * @return A Result object.
     */
    public Result leave(Player player) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't leave");

        boolean canLeave = Utility.callEvent(new FactionLeaveEvent(this, player));
        if (!canLeave) return Result.failure("EVENT_CANCEL",
                "Couldn't leave your faction");

        Language.sendMessage("command.faction.leave.success", player);

        return factionMemberManager.leave(player);
    }

    /**
     * Kick a player from the faction
     *
     * @param player The player to kick
     * @return A Result object.
     */
    public Result kick(OfflinePlayer player) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't kick");

        boolean canKick = Utility.callEvent(new FactionKickEvent(this, player));
        if (!canKick) return Result.failure("EVENT_CANCEL",
                "Couldn't kick §e" + player.getName());

        if (player.isOnline()) {
            Language.sendMessage("faction.kick.success", player.getPlayer(),
                    new Parseable("{faction_display}", displayName));
        } else {
            MessageSystem.sendMessage(player.getUniqueId(), Language.getMessage("faction.kick.success",
                    "en_us", new Parseable("{faction_display}", displayName)));
        }

        Result result = factionMemberManager.kick(player);

        if (!result.isSuccess()) return result;
        return new Result(true).setMessages("KICKED", "You kicked &e" + player.getName());
    }

    /**
     * Ban a player from the faction
     *
     * @param player The player to ban
     * @return A Result object.
     */
    public Result ban(OfflinePlayer player) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't ban");

        boolean canKick = Utility.callEvent(new FactionBanEvent(this, player));
        if (!canKick) return Result.failure("EVENT_CANCEL",
                "Couldn't ban §e" + player.getName());

        if (player.isOnline()) {
            Language.sendMessage("faction.kick.success", player.getPlayer());
        } else {
            MessageSystem.sendMessage(player.getUniqueId(), Language.getMessage("faction.kick.success", "en_us"));
        }

        Result result = factionMemberManager.ban(player);

        if (!result.isSuccess()) return result;
        return new Result(true).setMessages("BANNED", "You banned &e" + player.getName());
    }

    /**
     * This function will unban a player from the faction
     *
     * @param player The player to unban
     * @return A Result object.
     */
    public Result unban(OfflinePlayer player) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't unban");

        boolean canKick = Utility.callEvent(new FactionUnbanEvent(this, player));
        if (!canKick) return Result.failure("EVENT_CANCEL",
                "Couldn't unban §e" + player.getName());

        return factionMemberManager.pardon(player);
    }

    /**
     * This function kicks all members of the faction and then removes the faction from the LOADED_FACTIONS list
     *
     * @return A Result object.
     */
    public Result delete() {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't kick");
        AsyncTask.run(() -> {
            boolean canDelete = Utility.callEvent(new FactionDeleteEvent(this));
            if (!canDelete) return;

            ArrayList<UUID> memberCopy = new ArrayList<>(factionMemberManager.getMembers());
            for (UUID member : memberCopy) {
                kick(Bukkit.getOfflinePlayer(member));
            }

            ClaimManager manager = MainIF.getIF().getClaimManager();;
            Map<String, ArrayList<Claim>> claims = manager.CLAIMS;
            for (World world : Bukkit.getWorlds()) {
                claims.get(world.getName()).parallelStream()
                        .filter(x -> x.getRegistry().equals(registryName))
                        .forEach((claim) -> manager.removeProtection(world.getChunkAt(claim.getX(), claim.getY())));
            }

            LOADED_FACTIONS.remove(registryName);
            DataAccess.removeFile("Factions", registryName);
        });
        return Result.success();
    }

    public boolean isMember(OfflinePlayer player) {
        return factionMemberManager.getMembers().contains(player.getUniqueId());
    }

    @JsonIgnore
    public OpenType getOpenType() {
        return OpenType.values()[((EnumSetting) factionPerm.getFactionSettings().get("openType")).getSelected()];
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRegistryName() {
        return registryName;
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

    public FactionMemberManager getFactionMemberManager() {
        return factionMemberManager;
    }

    public Faction setFactionMemberManager(FactionMemberManager factionMemberManager) {
        this.factionMemberManager = factionMemberManager;
        return this;
    }

    public RelationManager getRelationManager() {
        return relationManager;
    }

    public void setRelationManager(RelationManager relationManager) {
        this.relationManager = relationManager;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getClaimedChunks() {
        return claimedChunks;
    }

    public void setClaimedChunks(int claimedChunks) {
        this.claimedChunks = claimedChunks;
    }

    public double getBalance() {
        if (!factionBank.hasBank) return 0;
        return factionBank.balance().balance;
    }

    public void setBalance(double amount) {
        if (factionBank == null) {
            factionBank = new FactionBank(this);
        }
        if (factionBank.hasBank) factionBank.deposit(amount);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public FactionPerm getFactionPerm() {
        return factionPerm;
    }

    public void setFactionPerm(FactionPerm factionPerm) {
        this.factionPerm = factionPerm;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public FactionBank getFactionBank() {
        return factionBank;
    }

    @JsonIgnore
    public void setFactionBank(FactionBank factionBank) {
        this.factionBank = factionBank;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public enum OpenType {PUBLIC, INVITE_ONLY, CLOSED}

    //</editor-fold>
}
