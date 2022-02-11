package io.github.toberocat.core.utility.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangMessage {
    public static List<LangDefaultDataAddon> langDefaultDataAddons = new ArrayList<>();

    public static final String PLUGIN_STANDBY_MESSAGE = "message.plugin-standby";

    public static final String GUI_BACKUP_CONFIG_TITLE = "gui.config.backup.title";

    public static final String ERROR_GENERAL = "error.general";

    public static final String COMMAND_HELP_DESCRIPTION = "command.help.description";
    public static final String THIS_COMMAND_DOES_NOT_EXIST = "command.not-exist";

    public static final String GUI_EXTENSION_DOWNLOAD_TITLE = "gui.extension.download.title";

    //<editor-fold desc="Command Config">
    public static final String COMMAND_CONFIG_DESCRIPTION = "command.config.description";
    public static final String COMMAND_CONFIG_BACKUP_DESCRIPTION = "command.config.backup.description";
    public static final String COMMAND_CONFIG_SAVE_DESCRIPTION = "command.config.save.description";
    public static final String COMMAND_CONFIG_SAVE_SUCCESS = "command.config.save.success";
    public static final String COMMAND_CONFIG_SAVE_BACKUP = "command.config.save.backup";
    public static final String COMMAND_CONFIG_RELOAD_DESCRIPTION = "command.config.reload.description";
    public static final String COMMAND_CONFIG_RELOAD_SUCCESS = "command.config.reload.success";
    public static final String COMMAND_CONFIG_BACKUP_REMOVE_DESCRIPTION = "command.config.backup.remove.description";
    public static final String COMMAND_CONFIG_BACKUP_REMOVE_SUCCESS = "command.config.backup.remove.success";
    public static final String COMMAND_CONFIG_CONFIGURE_DESCRIPTION = "command.config.configure.description";
    //</editor-fold>

    //<editor-fold desc="Command Relation">
    public static final String COMMAND_RELATION_DESCRIPTION = "command.relation.description";
    public static final String COMMAND_RELATION_ALLY_INVITATION = "command.relation.ally.invitation";
    public static final String COMMAND_RELATION_STATUS_CHANGE = "command.relation.status.changed";

    public static final String COMMAND_RELATION_ACCEPT_DESCRIPTION = "command.relation.accept.description";
    public static final String COMMAND_RELATION_ACCEPT_SUCCESS = "command.relation.accept.success";
    public static final String COMMAND_RELATION_REJECT_DESCRIPTION = "command.relation.reject.description";
    public static final String COMMAND_RELATION_REJECT_SUCCESS = "command.relation.reject.success";

    public static final String COMMAND_RELATION_ALLY_DESCRIPTION = "command.relation.ally.description";
    public static final String COMMAND_RELATION_ENEMY_DESCRIPTION = "command.relation.enemy.description";
    public static final String COMMAND_RELATION_REMOVE_DESCRIPTION = "command.relation.remove.description";

    public static final String COMMAND_RELATION_ALLY_SUCCESS = "command.relation.ally.success";
    public static final String COMMAND_RELATION_ALLY_FAIL = "command.relation.ally.fail";
    public static final String COMMAND_RELATION_ENEMY_SUCCESS = "command.relation.enemy.success";
    public static final String COMMAND_RELATION_ENEMY_FAIL = "command.relation.enemy.fail";
    public static final String COMMAND_RELATION_REMOVE_SUCCESS = "command.relation.remove.success";
    public static final String COMMAND_RELATION_REMOVE_FAIL = "command.relation.remove.fail";
    //</editor-fold>

    //<editor-fold desc="Command Faction">
    public static final String COMMAND_FACTION_CREATE_DESCRIPTION = "command.faction.create.description";
    public static final String COMMAND_FACTION_CREATE_SUCCESS = "command.faction.create.success";
    public static final String COMMAND_FACTION_CREATE_FAILED = "command.faction.create.failed";
    public static final String COMMAND_FACTION_DELETE_DESCRIPTION = "command.faction.delete.description";
    public static final String COMMAND_FACTION_DELETE_SUCCESS = "command.faction.delete.success";
    public static final String COMMAND_FACTION_DELETE_FAILED = "command.faction.delete.failed";
    public static final String COMMAND_FACTION_CLAIM_ONE_SUCCESS = "command.faction.claim.one.success";

    public static final String COMMAND_FACTION_CLAIM_DESCRIPTION = "command.faction.claim.description";
    public static final String COMMAND_FACTION_CLAIM_ONE_DESCRIPTION = "command.faction.claim.one.description";
    public static final String COMMAND_FACTION_CLAIM_ONE_FAILED = "command.faction.claim.one.failed";

    public static final String COMMAND_FACTION_LEAVE_SUCCESS = "command.faction.leave.success";
    public static final String COMMAND_FACTION_JOIN_SUCCESS = "command.faction.join.success";
    //</editor-fold>

    //<editor-fold desc="General Faction">
    public static final String FACTION_LEAVE_DELETED = "faction.leave.forced";
    public static final String FACTION_KICKED = "faction.kick.success";
    //</editor-fold>

    //<editor-fold desc="Command Plugin">
    public static final String COMMAND_PLUGIN_DESCRIPTION = "command.plugin.description";
    public static final String COMMAND_PLUGIN_DISABLE_DESCRIPTION = "command.plugin.disable.description";
    public static final String COMMAND_PLUGIN_DISABLE_SUCCESS = "command.plugin.disable.success";
    public static final String COMMAND_PLUGIN_STANDBY_DESCRIPTION = "command.plugin.standby.description";
    public static final String COMMAND_PLUGIN_STANDBY_SUCCESS = "command.plugin.standby.success";
    //</editor-fold>

    //<editor-fold desc="Command Zones">
    public static final String COMMAND_ZONES_DESCRIPTION = "command.zones.description";
    public static final String COMMAND_ZONES_UNCLAIM = "command.zones.unclaim";
    public static final String COMMAND_ZONES_UNCLAIM_DESCRIPTION = "command.zones.unclaim.description";
    public static final String COMMAND_ZONES_SAFEZONE_DESCRIPTION = "command.zones.safezone.description";
    public static final String COMMAND_ZONES_SAFEZONE_CLAIM = "command.zones.safezone.claim";
    public static final String COMMAND_ZONES_WARZONE_DESCRIPTION = "command.zones.warzone.description";
    public static final String COMMAND_ZONES_WARZONE_CLAIM = "command.zones.warzone.claim";
    public static final String COMMAND_ZONES_UNCLAIMABLE_DESCRIPTION = "command.zones.unclaimable.description";
    public static final String COMMAND_ZONES_UNCLAIMABLE_CLAIM = "command.zones.unclaimable.claim";
    //</editor-fold>

    //<editor-fold desc="Command Admin">
    public static final String COMMAND_ADMIN_DESCRIPTION = "command.admin.description";
    public static final String COMMAND_ADMIN_HARD_RESET_DESCRIPTION = "command.admin.hardreset.description";
    public static final String COMMAND_ADMIN_HARD_RESET_CONFIRM_PLAYER = "command.admin.hardreset.confirm_player";
    public static final String COMMAND_ADMIN_HARD_RESET_SUCCESS = "command.admin.hardreset.success";
    public static final String COMMAND_ADMIN_DISBAND_DESCRIPTION = "command.admin.disband.description";
    public static final String COMMAND_ADMIN_DISBAND_SUCCESS = "command.admin.disband.success";
    //</editor-fold>

    //<editor-fold desc="Rank">
    public static final String RANK_OWNER_DESCRIPTION = "rank.owner.description";
    public static final String RANK_ADMIN_DESCRIPTION = "rank.admin.description";
    public static final String RANK_MEMBER_DESCRIPTION = "rank.member.description";
    public static final String RANK_NEWMEMBER_DESCRIPTION = "rank.newmember.description";
    public static final String RANK_GUEST_DESCRIPTION = "rank.guest.description";
    //</editor-fold>

    public static final String TERRITORY_ENTERED_CHAT = "territory.entered.chat";
    public static final String TERRITORY_ENTERED_ACTIONBAR = "territory.entered.actionbar";
    public static final String TERRITORY_ENTERED_TITLE = "territory.entered.title";
    public static final String TERRITORY_ENTERED_SUBTITLE = "territory.entered.subtitle";
    public static final String TERRITORY_WILDERNESS = "territory.wilderness";
    public static final String TERRITORY_SAFEZONE = "territory.safezone";
    public static final String TERRITORY_WARZONE= "territory.warzone";



    //<editor-fold desc="Command Settings">
    public static final String COMMAND_SETTINGS_DESCRIPTION = "command.settings.description";
    public static final String COMMAND_SETTINGS_PLAYER_DESCRIPTION = "command.settings.player.description";
    //</editor-fold>


    private Map<String, String> messages;
    private String author;
    private String credits;
    private String version;

    public LangMessage() {
        messages = new HashMap<>();
        author = "Tobero";
        credits = "Thanks for this amazing support from everyone who uses this plugin";
        version = "1.0";

        messages.put(PLUGIN_STANDBY_MESSAGE, "&fThe plugin has put itself in standby mode. &f&nAll&f commands are now disabled. &f&nOnly&f simple claim protection is running. &f&nCheck the console for more details");
        messages.put(GUI_BACKUP_CONFIG_TITLE, "&e&lImprovedFaction &econfig backup");

        messages.put(THIS_COMMAND_DOES_NOT_EXIST, "&cThis command doesn't exist");
        messages.put(ERROR_GENERAL, "&c{error}");

        messages.put(COMMAND_HELP_DESCRIPTION, "&fGet a list of all first layer commands");

        messages.put(GUI_EXTENSION_DOWNLOAD_TITLE, "&eAvailable extensions");

        //<editor-fold desc="Command Config">
        messages.put(COMMAND_CONFIG_DESCRIPTION, "&fAccess the config commands");
        messages.put(COMMAND_CONFIG_BACKUP_DESCRIPTION, "&fManage what data gets restored from the backup files");

        messages.put(COMMAND_CONFIG_SAVE_DESCRIPTION, "&fSave the config data. &8Note: &fThis will maybe save some files to the backup");
        messages.put(COMMAND_CONFIG_SAVE_SUCCESS, "&fSuccessfully saved {config}");
        messages.put(COMMAND_CONFIG_SAVE_BACKUP, "&f&6&lWarning:&f looks like something went wrong while saving {config}. Please check &7console&f / &7reload&f. When done, use &7/f config backup");

        messages.put(COMMAND_CONFIG_RELOAD_DESCRIPTION, "&fReload the configs. &8Note: &fThis will &f&l&nnot&f save the configs before reloading");
        messages.put(COMMAND_CONFIG_RELOAD_SUCCESS, "&a&lSuccessfully&f reloaded the configs");

        messages.put(COMMAND_CONFIG_BACKUP_REMOVE_DESCRIPTION, "&fThis will remove all loaded backup files. &6&lWarning:&f This cannot be undone");
        messages.put(COMMAND_CONFIG_BACKUP_REMOVE_SUCCESS, "&a&lSuccessfully&f removed all backups");

        messages.put(COMMAND_CONFIG_CONFIGURE_DESCRIPTION, "&fConfigure a config within the game by using a gui." +
                "\n&8Note:&f This wil also give hints, tips and usage examples in the itemlore");
        //</editor-fold>

        //<editor-fold desc="Command Faction">
        messages.put(COMMAND_FACTION_CREATE_DESCRIPTION, "&fCreate a new faction");
        messages.put(COMMAND_FACTION_CREATE_SUCCESS, "&a&lSuccessfully&f created your faction {faction_name}");
        messages.put(COMMAND_FACTION_CREATE_FAILED, "&c&lFailed&f to create your faction. {error}");

        messages.put(COMMAND_FACTION_DELETE_DESCRIPTION, "&fDelete a faction if you are the owner of it");
        messages.put(COMMAND_FACTION_DELETE_SUCCESS, "&a&lSuccessfully&f deleted your faction");
        messages.put(COMMAND_FACTION_DELETE_FAILED, "&c&lFailed&f to delete the faction. {error}");

        messages.put(COMMAND_FACTION_CLAIM_DESCRIPTION, "&fAccess the claim options");
        messages.put(COMMAND_FACTION_CLAIM_ONE_DESCRIPTION, "&fProtect a chunk from being grief by other factions");
        messages.put(COMMAND_FACTION_CLAIM_ONE_FAILED, "&c&lFailed&c. {error}");
        messages.put(COMMAND_FACTION_CLAIM_ONE_SUCCESS, "&a&lSuccessfully&f claim this chunk");

        messages.put(COMMAND_FACTION_JOIN_SUCCESS, "&a&lSuccessfully&f joined the faction");
        messages.put(COMMAND_FACTION_LEAVE_SUCCESS, "&a&lSuccessfully&f left the faction");
        //</editor-fold>

        //<editor-fold desc="General Faction">
        messages.put(FACTION_LEAVE_DELETED, "&e{faction_display}&f got &c&ldeleted&f. You left it automatically");
        messages.put(FACTION_KICKED, "&c&lSorry!&f But you got kicked out of &e{faction_display}");
        //</editor-fold>

        //<editor-fold desc="Ranks">
        messages.put(RANK_OWNER_DESCRIPTION, "The &6owner&8 has created this faction. He has all rights, even if you remove them");
        messages.put(RANK_ADMIN_DESCRIPTION, "The &6admin&8 is like a moderator. He should be able to help people in your faction");
        messages.put(RANK_MEMBER_DESCRIPTION, "The &6member&8 is automatically everyone which is in your faction for more than 3 days");
        messages.put(RANK_NEWMEMBER_DESCRIPTION, "The &6new member&8 is like a member, but he just joined. This rank exists, because else anyone could join a public faction to raid them. So don't give them access to all your valuables");
        messages.put(RANK_GUEST_DESCRIPTION, "This rank has everyone whom is not in your faction. Can be used as an example to allow button presses by anyone, if you have a casino in your faction");
        //</editor-fold>

        //<editor-fold desc="Command Relations">
        messages.put(COMMAND_RELATION_DESCRIPTION, "&fManage your relations");
        messages.put(COMMAND_RELATION_STATUS_CHANGE, "&fYour status with {faction} changed. You are now {status}");

        messages.put(COMMAND_RELATION_ACCEPT_DESCRIPTION, "&fAccept a relation with a faction for being allies");
        messages.put(COMMAND_RELATION_ACCEPT_SUCCESS, "&a&fSuccessfully&f accepted the invitation of &e{faction}&f. You are now allies. Change their permissions using the rank system");

        messages.put(COMMAND_RELATION_REJECT_DESCRIPTION, "&fReject a invitation for being allies");
        messages.put(COMMAND_RELATION_REJECT_SUCCESS, "&a&lSuccessfully&c rejected&f the invitation of &e{faction}&f to be allies");

        messages.put(COMMAND_RELATION_ALLY_INVITATION, "&e{faction}&f wants to be allied with you. You want to accept it? Use &8/f relation accept {inviter}&f or &8/f relation reject {inviter}");
        messages.put(COMMAND_RELATION_ALLY_DESCRIPTION, "&fSet a faction to your allies. Set their permissions with the rank");
        messages.put(COMMAND_RELATION_ALLY_SUCCESS, "&a&lSuccessfully&f set {faction} to your allies");
        messages.put(COMMAND_RELATION_ALLY_FAIL, "&c&lFailed&f to set {faction} to your allies. Caused by {error}");

        messages.put(COMMAND_RELATION_ENEMY_DESCRIPTION, "&fSet a faction to your &cenemies&f. Set their permissions with the rank");
        messages.put(COMMAND_RELATION_ENEMY_SUCCESS, "&a&lSuccessfully&f set {faction} to your &cenemies");
        messages.put(COMMAND_RELATION_ENEMY_FAIL, "&c&lFailed&f to set {faction} to your enemies. Caused by {error}");

        messages.put(COMMAND_RELATION_REMOVE_DESCRIPTION, "&fSet a faction back to being neutral");
        messages.put(COMMAND_RELATION_REMOVE_SUCCESS, "&a&lSuccessfully&f set {faction} to be hostile towards you");
        messages.put(COMMAND_RELATION_REMOVE_FAIL, "&c&lFailed&f to set {faction} to be hostile towards you. Caused by {error}");
        //</editor-fold>

        //<editor-fold desc="Command Plugin">
        messages.put(COMMAND_PLUGIN_DESCRIPTION, "&fManage the plugin commands");
        messages.put(COMMAND_PLUGIN_DISABLE_DESCRIPTION, "&fDisable the plugin, so you can remove it without stoping the server");
        messages.put(COMMAND_PLUGIN_DISABLE_SUCCESS, "&a&lSuccessfully&f disabled the plugin. &8Note:&7 This should only be used if you want to remove the plugin &7&n&lnot&7 reload it");

        messages.put(COMMAND_PLUGIN_STANDBY_DESCRIPTION, "&fPut the plugin manually in standby. &7Use cases:&f You have a server and don't want to shut down for changes. Put if in standby to protect the claimed chunks and prevent errors with the data");
        messages.put(COMMAND_PLUGIN_STANDBY_SUCCESS, "&a&lSuccessfully&f put the plugin in &l&eStandby&f. Use &7/rl&f to disable standby");
        //</editor-fold>

        //<editor-fold desc="Command Zones">
        messages.put(COMMAND_ZONES_DESCRIPTION, "&fCreate & manage unclaimable neutral zones for players");
        messages.put(COMMAND_ZONES_UNCLAIM, "&c&lUnclaimed&f the chunk from {chunktype}");
        messages.put(COMMAND_ZONES_UNCLAIM_DESCRIPTION, "&c&lUnclaimed&f &f&nany&f chunk");
        messages.put(COMMAND_ZONES_SAFEZONE_DESCRIPTION, "&fClaim a safezone, where nobody can place blocks, break blocks, hit each other and claim this chunk");
        messages.put(COMMAND_ZONES_SAFEZONE_CLAIM, "&a&lClaimed&f the chunk with &b&lSafezone");
        messages.put(COMMAND_ZONES_WARZONE_DESCRIPTION, "&fClaim a warzone, where nobody can place blocks, break blocks, claim this chunk but hit each others");
        messages.put(COMMAND_ZONES_WARZONE_CLAIM, "&a&lClaimed&f the chunk with &4&lWarzone");
        messages.put(COMMAND_ZONES_UNCLAIMABLE_DESCRIPTION, "&fClaim a unclaimable zone, where nobody can claim this chunk. That's all. It allows everything, but claiming. Perfect for using worldguard to set flags");
        messages.put(COMMAND_ZONES_UNCLAIMABLE_CLAIM, "&a&lClaimed&f the chunk with unclaimedable");
        //</editor-fold>

        //<editor-fold desc="Command Admin">
        messages.put(COMMAND_ADMIN_DESCRIPTION, "&fThis command allows access to command that should be used to moderate your server. &8Note: &fThis can also be used to delete the entire faction data");
        messages.put(COMMAND_ADMIN_HARD_RESET_DESCRIPTION, "&6&lWarning: &fThis will remove all your data. Removes all /Data/ files and resets your configs");
        messages.put(COMMAND_ADMIN_HARD_RESET_CONFIRM_PLAYER, "&6&lWarning: &fYou are about to delete your data. &fPlease confirm intention to protect you to prevent you from inadvertently deleting your data. Use &8/f admin reset confirm &f if you want to use it");
        messages.put(COMMAND_ADMIN_HARD_RESET_SUCCESS, "&a&lSuccessfully&f removed all your data. Please use &8/rl&f to make sure everything loaded correctly");
        messages.put(COMMAND_ADMIN_DISBAND_DESCRIPTION, "&fDelete a faction. &6&lWarning:&f This cannot be undone. If you want to delete your own faction, please use &7/f delete");
        messages.put(COMMAND_ADMIN_DISBAND_SUCCESS, "&a&lSuccessfully &f deleted {faction_display}. &f&nEveryone&f got kicked");
        //</editor-fold>

        messages.put(TERRITORY_ENTERED_CHAT, "Entered {relation}{territory}");
        messages.put(TERRITORY_ENTERED_ACTIONBAR, "{relation}{territory}");
        messages.put(TERRITORY_ENTERED_TITLE, "{relation}{territory}");
        messages.put(TERRITORY_ENTERED_SUBTITLE, "{relation}{territory}");
        messages.put(TERRITORY_WILDERNESS, "&2Wilderness");
        messages.put(TERRITORY_SAFEZONE, "&bSafezone");
        messages.put(TERRITORY_WARZONE, "&4Warzone");

        //<editor-fold desc="Command Settings">
        messages.put(COMMAND_SETTINGS_DESCRIPTION, "&fManage faction & player settings");
        messages.put(COMMAND_SETTINGS_PLAYER_DESCRIPTION, "&fChange values for your self");
        //</editor-fold>


        for (LangDefaultDataAddon addon : langDefaultDataAddons) {
            messages.putAll(addon.Add());
        }
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
