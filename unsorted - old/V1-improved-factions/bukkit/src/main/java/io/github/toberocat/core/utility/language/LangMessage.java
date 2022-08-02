package io.github.toberocat.core.utility.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.MainIF;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LangMessage {

    private static final Map<String, String> defaults = new HashMap<>();

    private Map<String, String> messages;
    private String author;
    private String credits;
    private String version;

    public LangMessage() {
        messages = new HashMap<>();

        /**
         author = "Tobero";
         credits = "Thanks for this amazing support";
         version = MainIF.getVersion().getVersion();

         messages.put(PLUGIN_STANDBY_MESSAGE, "&fThe plugin has put itself in standby mode. &f&nAll&f commands are now disabled. &f&nOnly&f simple claim protection is running. &f&nCheck the console for more details");
         messages.put(GUI_BACKUP_CONFIG_TITLE, "&e&lImprovedFaction &econfig backup");

         messages.put(THIS_COMMAND_DOES_NOT_EXIST, "&cThis command doesn't exist");
         messages.put(ERROR_GENERAL, "&c{error}");

         messages.put(COMMAND_HELP_DESCRIPTION, "&fGet a list of all first layer commands");

         messages.put(GUI_EXTENSION_DOWNLOAD_TITLE, "&eAvailable extensions");

         //<editor-fold desc="Papi">
         messages.put(NO_FACTION_PAPI, "No faction");
         //</editor-fold>

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

         messages.put(COMMAND_FACTION_CLAIM_DESCRIPTION, "&fProtect a chunk from being grief by other factions");
         messages.put(COMMAND_FACTION_CLAIM_FAILED, "&c&lFailed&c. {error}");
         messages.put(COMMAND_FACTION_CLAIM_SUCCESS, "&a&lSuccessfully&f claim this chunk");

         messages.put(COMMAND_FACTION_JOIN_SUCCESS, "&a&lSuccessfully&f joined the faction");
         messages.put(COMMAND_FACTION_LEAVE_SUCCESS, "&a&lSuccessfully&f left the faction");
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
         messages.put(COMMAND_ADMIN_BYPASS_DESCRIPTION, "&fThis allows you to bypass chunk protection");
         messages.put(COMMAND_ADMIN_FREEZE_DESCRIPTION, "&b&lFreeze&f a faction, so that nobody can join or leave it. Use it when the faction got reported");
         messages.put(COMMAND_ADMIN_GET_PLAYER_FACTION_DESCRIPTION, "&fGet in what faction a player is, by accessing it from the saved persistend data");
         messages.put(COMMAND_ADMIN_IF_PLAYER_FACTION_DESCRIPTION, "&fGet if a player is in a faction or not");
         messages.put(COMMAND_ADMIN_MIGRATE_DESCRIPTION, "&fMigrate data from the old plugins files to the new ones");
         messages.put(COMMAND_ADMIN_PERMANENT_DESCRIPTION, "&fToggle between if a faction is permanent. Permanent factions still exists even when they are empty");
         messages.put(COMMAND_ADMIN_REGENERATE_DESCRIPTION, "&fThis allows you to regenerate all persistent data");
         messages.put(COMMAND_ADMIN_TIMEOUT_DESCRIPTION, "&fTimeout a player from joining another faction");
         messages.put(COMMAND_ADMIN_DELTIMEOUT_DESCRIPTION, "&fRemove a timeout from a player");
         messages.put(COMMAND_ADMIN_JOIN_PRIVATE_DESCRIPTION, "&fJoina faction, ignoring it openstate, silently");
         //</editor-fold>
         //<editor-fold desc="Command Settings">
         messages.put(COMMAND_SETTINGS_DESCRIPTION, "&fManage faction & player settings");
         messages.put(COMMAND_SETTINGS_PLAYER_DESCRIPTION, "&fChange values for your self");
         //</editor-fold>
         //<editor-fold desc="Command Extension">
         messages.put(COMMAND_EXTENSION_DESCRIPTION, "&fAdd new community made features to the plugin");
         messages.put(COMMAND_EXTENSION_DOWNLOAD_DESCRIPTION, "&fDownload a extension from the gui");
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
         //<editor-fold desc="Territory">
         messages.put(TERRITORY_ENTERED_CHAT, "Entered {relation}{territory}");
         messages.put(TERRITORY_ENTERED_ACTIONBAR, "{relation}{territory}");
         messages.put(TERRITORY_ENTERED_TITLE, "{relation}{territory}");
         messages.put(TERRITORY_ENTERED_SUBTITLE, "{relation}{territory}");
         messages.put(TERRITORY_WILDERNESS, "&2Wilderness");
         messages.put(TERRITORY_SAFEZONE, "&bSafezone");
         messages.put(TERRITORY_WARZONE, "&4Warzone");
         //</editor-fold>
         **/

    }

    public static void addDefault(LangMessage message) {
        defaults.putAll(message.messages);
    }

    public static LangMessage createNewLang() {
        InputStream stream = LangMessage.class.getResourceAsStream("/lang/en_us.lang");
        ObjectMapper om = new ObjectMapper();
        try {
            LangMessage message = om.readValue(stream, LangMessage.class);
            if (message == null) throw new IOException();

            message.messages.putAll(defaults);
            return message;
        } catch (IOException e) {
            MainIF.logMessage(Level.WARNING, "&cCouldn't create base instance of the lang files");
            return new LangMessage();
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
