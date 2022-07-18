package io.github.toberocat.core.factions.local;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.OpenType;
import io.github.toberocat.core.factions.claim.FactionClaims;
import io.github.toberocat.core.factions.local.FactionDatabaseHandler;
import io.github.toberocat.core.factions.local.bank.FactionBank;
import io.github.toberocat.core.factions.local.members.FactionMemberManager;
import io.github.toberocat.core.factions.local.modules.FactionModule;
import io.github.toberocat.core.factions.local.modules.MessageModule;
import io.github.toberocat.core.factions.local.permission.FactionPerm;
import io.github.toberocat.core.factions.local.power.PowerManager;
import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.factions.local.rank.members.AdminRank;
import io.github.toberocat.core.factions.local.rank.members.OwnerRank;
import io.github.toberocat.core.factions.local.relation.RelationManager;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.claim.WorldClaims;
import io.github.toberocat.core.utility.color.FactionColors;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.date.DateCore;
import io.github.toberocat.core.utility.events.faction.*;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.messages.MessageSystem;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import io.github.toberocat.core.utility.data.database.sql.MySqlDatabase;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class LocalFaction implements Faction {

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
    private LinkedHashMap<String, FactionModule> modules = new LinkedHashMap<>();

    /**
     * Don't use this. it's for jackson (json).
     */
    public LocalFaction() {
    }

    public LocalFaction(@NotNull String displayName, @NotNull UUID owner) {
        this.registryName = Faction.displayToRegistry(displayName);
        this.displayName = displayName.substring(0, MainIF.config().getInt("faction.maxNameLen"));

        FileConfiguration config = MainIF.config();
        this.motd = config.getString("faction.default.motd", "Improved faction");
        this.tag = config.getString("faction.default.tag", "IFF");
        this.motd = config.getString("faction.default.motd", "New faction");
        this.tag = config.getString("faction.default.tag", "IFF");

        this.permanent = config.getBoolean("faction.default.permanent", true);
        this.frozen = config.getBoolean("faction.default.frozen", true);

        this.description = new String[]{"&eCool &cfaction"};
        this.claimedChunks = 0;
        this.owner = owner;
        this.createdAt = DateCore.TIME_FORMAT.print(new LocalDateTime());

        /* Managers */
        this.powerManager = new PowerManager(this, MainIF.config().getInt("faction.default.power.max"));
        this.factionMemberManager = new FactionMemberManager(this);
        this.relationManager = new RelationManager(this);
        this.factionBank = new FactionBank();
        this.factionPerm = new FactionPerm(this);

    }

    public @NotNull Rank getPlayerRank(OfflinePlayer player) {
        return factionPerm.getPlayerRank(player);
    }

    public boolean hasPermission(OfflinePlayer player, String permission) {
        Rank rank = getPlayerRank(player);
        if (rank == null) return false;
        if (!factionPerm.getRankSetting().containsKey(permission)) return false;
        return factionPerm.getRankSetting().get(permission).hasPermission(rank);
    }

    @Override
    public boolean isMember(@NotNull UUID player) {
        return false;
    }

    @Override
    public void changeRank(@NotNull OfflinePlayer player, @NotNull Rank rank) {

    }

    @Override
    public void transferOwnership(@NotNull Player player) {
        OfflinePlayer currentOwner = Bukkit.getOfflinePlayer(owner);

        factionPerm.setRank(player, OwnerRank.registry);
        factionPerm.setRank(currentOwner, AdminRank.registry);

        Bukkit.getServer().getPluginManager().callEvent(new FactionTransferOwnershipEvent(this,
                currentOwner, player));
    }

    @Override
    public void deleteFaction() {

    }

    @Override
    public boolean joinPlayer(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull Rank rank) {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't join");

        boolean canJoin = Utility.callEvent(new FactionJoinEvent(this, player));
        if (!canJoin) return Result.failure("EVENT_CANCEL",
                "Couldn't join faction");

        return factionMemberManager.join(player);
    }

    @Override
    public boolean joinPlayer(@NotNull Player player, @NotNull UUID inviteId) {
        return false;
    }

    @Override
    public boolean leavePlayer(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean kickPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean banPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean pardonPlayer(@NotNull OfflinePlayer player) {
        return false;
    }

    @Override
    public @NotNull BigDecimal getPower() {
        return null;
    }

    @Override
    public double playerPower(@NotNull OfflinePlayer player) {
        return 0;
    }

    @Override
    public boolean addAlly(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public boolean addEnemy(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public boolean resetRelation(@NotNull LocalFaction faction) {
        return false;
    }

    @Override
    public FactionClaims getClaims() {
        return null;
    }

    @Override
    public <C> @Nullable C getModule(@NotNull Class<C> clazz) {
        return null;
    }

    @Override
    public <C> void createModule(@NotNull Class<C> clazz, Object... parameters) {

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


        Result result = factionMemberManager.kick(player);

        if (!result.isSuccess()) return result;
        if (player.isOnline()) {
            Language.sendMessage("faction.kick.success", player.getPlayer(),
                    new Parseable("{faction_display}", displayName));
        } else {
            MessageSystem.sendMessage(player.getUniqueId(), Language.getMessage("faction.kick.success",
                    "en_us", new Parseable("{faction_display}", displayName)));
        }
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
    public Result<?> delete() {
        if (frozen) return Result.failure("FROZEN", "This faction is frozen. You can't kick");
        AsyncTask.run(() -> {
            boolean canDelete = Utility.callEvent(new FactionDeleteEvent(this));
            if (!canDelete) return;

            ArrayList<UUID> memberCopy = new ArrayList<>(factionMemberManager.getMembers());
            for (UUID member : memberCopy) {
                kick(Bukkit.getOfflinePlayer(member));
            }

            ClaimManager manager = MainIF.getIF().getClaimManager();

            Map<String, WorldClaims> claims = manager.CLAIMS;
            LinkedList<Chunk> rmProtection = new LinkedList<>();

            claims.entrySet().stream()
                    .filter(x -> x != null && x.getKey() != null &&
                            Bukkit.getWorld(x.getKey()) != null && x.getValue() != null)
                    .forEach((entry) -> {
                        World world = Bukkit.getWorld(entry.getKey());
                        if (world == null) return;

                        entry.getValue().stream()
                                .filter(x -> Objects.nonNull(x) && x.getRegistry().equals(registryName))
                                .forEach(c -> rmProtection.add(world.getChunkAt(c.getX(), c.getY())));
                    });

            rmProtection.forEach(manager::removeProtection);
            rmProtection.clear();

            LOADED_FACTIONS.remove(registryName);
            FileAccess.delete("Factions", registryName, this);
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

    @JsonIgnore
    public MessageModule getMessageModule() {
        if (!modules.containsKey(FactionModule.MESSAGE_MODULE_ID))
            modules.put(FactionModule.MESSAGE_MODULE_ID, new MessageModule(this));

        return (MessageModule) modules.get(FactionModule.MESSAGE_MODULE_ID);
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

    @Override
    public void setDisplay(@NotNull String display) {

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

    @Override
    public void createFromStorage(@NotNull String loadRegistry) {

    }

    @Override
    public @NotNull String getRegistry() {
        return null;
    }

    @Override
    public @NotNull String getDisplay() {
        return null;
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

    @Override
    public io.github.toberocat.core.factions.@NotNull OpenType getType() {
        return null;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt.replace('T', ' ');
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

    @Override
    public void setType(io.github.toberocat.core.factions.@NotNull OpenType type) {

    }

    public LinkedHashMap<String, FactionModule> getModules() {
        return modules;
    }

    public Faction setModules(LinkedHashMap<String, FactionModule> modules) {
        this.modules = modules;
        return this;
    }

    @JsonIgnore
    public int getColor() {
        if (getFactionPerm().getFactionSettings().get("universal_color") instanceof EnumSetting setting) {
            FactionColors color = FactionColors.values()[setting.getSelected()];
            return color.getColor();
        }
        return FactionColors.RED.getColor();
    }

    public enum OpenType {PUBLIC, INVITE_ONLY, CLOSED}

    //</editor-fold>
}
