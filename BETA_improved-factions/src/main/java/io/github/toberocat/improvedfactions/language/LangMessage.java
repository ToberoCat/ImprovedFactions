package io.github.toberocat.improvedfactions.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LangMessage {

    public static final String ADMIN_RANK_DESCRIPTION_LINE_1 = "rank.admin.desc.line.1";
    public static final String ADMIN_RANK_DESCRIPTION_LINE_2 = "rank.admin.desc.line.2";

    public static final String BANNED_PLAYER_COMMAND_SUCCESS = "command.ban.success";
    public static final String BANNED_PLAYER_COMMAND_LEAVE = "command.ban.leave";
    public static final String BANNED_PLAYER_COMMAND_ALREADY = "command.ban.already-done";
    public static final String BANNED_PLAYER_COMMAND_DESCRIPTION = "command.ban.description";

    public static final String INFO_DESCRIPTION = "command.info.description";

    public static final String RULES_ARE_EMPTY = "commands.rules.empty";
    public static final String RULES_SET_COMMAND = "commands.rules.set.success";
    public static final String RULES_DESCRIPTION = "command.rules.description";

    public static final String AUTO_CLAIM_ENABLED = "command.claim.auto.enabled";
    public static final String AUTO_CLAIM_DESCRIPTION = "command.claim.auto.description";
    public static final String AUTO_CLAIM_DISABLED = "command.claim.auto.disabled";

    public static final String AUTO_UNCLAIM_ENABLED = "command.unclaim.auto.enabled";
    public static final String AUTO_UNCLAIM_DESCRIPTION = "command.unclaim.auto.description";
    public static final String AUTO_UNCLAIM_DISABLED = "command.unclaim.auto.disabled";

    public static final String CLAIM_ONE_DESCRIPTION = "command.claim.one.description";
    public static final String CLAIM_ONE_NO_POWER = "command.claim.one.no-power";
    public static final String CLAIM_ONE_SUCCESS = "command.claim.one.success";
    public static final String CLAIM_ONE_NOT_CONNECTED = "command.claim.one.not-connected";
    public static final String CLAIM_ONE_ALREADY_PROPERTY = "command.claim.one.already-property";
    public static final String CLAIM_ONE_OWNED_BY_OTHERS = "command.claim.one.owned-by-others";

    public static final String UNCLAIM_ONE_DESCRIPTION = "command.unclaim.one.description";
    public static final String UNCLAIM_ONE_SOMETHING_WRONG = "command.unclaim.one.something-went-wrong";
    public static final String UNCLAIM_ONE_SUCCESS = "command.unclaim.one.success";
    public static final String UNCLAIM_ONE_DISCONNECTED = "command.unclaim.one.disconnected";
    public static final String UNCLAIM_ONE_ALREADY_WILDNESS = "command.unclaim.one.already-wildness";
    public static final String UNCLAIM_ONE_NOT_YOUR_PROPERTY = "command.unclaim.one.not-your-property";

    public static final String CLAIM_DESCRIPTION = "command.claim.description";
    public static final String UNCLAIM_DESCRIPTION = "command.unclaim.description";

    public static final String RANK_PERMISSIONS_GUI = "command.rank.permGUI.description";
    public static final String RANK_SET = "command.rank.set.description";
    public static final String RANK_SET_SUCCESS = "command.rank.set.success";

    public static final String CREATE_DESCRIPTION = "command.create.description";
    public static final String CREATE_ALREADY_EXISTS = "command.create.faction-already-existing";
    public static final String CREATE_NEED_NAME = "command.create.need-name";
    public static final String CREATE_ALREADY_IN_FACTION = "command.create.already-in-faction";
    public static final String CREATE_NO_COLOR_IN_NAME_PERM = "command.create.no-color-in-name-perm";
    public static final String CREATE_SUCCESS = "command.create.success";
    public static final String CREATE_CANNOT_JOIN = "command.create.cannot-join";

    public static final String DELETE_DESCRIPTION = "command.delete.description";
    public static final String DELETE_SUCCESS = "command.delete.success";
    public static final String DELETE_ERROR = "command.delete.error";

    public static final String DESCRIPTION_DESCRIPTION = "command.description.description";
    public static final String DESCRIPTION_SUCCESS = "command.description.success";

    public static final String HELP_DESCRIPTION = "command.help.description";

    public static final String INVITE_DESCRIPTION = "command.invite.description";
    public static final String INVITE_SUCCESS_SENDER = "command.invite.success-sender";
    public static final String INVITE_SUCCESS_RECEIVER = "command.invite.success-receiver";
    public static final String INVITE_HOVER_EVENT = "command.invite.hover-event";

    public static final String JOIN_DESCRIPTION = "command.join.description";
    public static final String JOIN_ERROR_NO_FACTION_FOUND = "command.join.no-faction-found";
    public static final String JOIN_ERROR_FACTION_PRIVATE = "command.join.faction-private";
    public static final String JOIN_ERROR_FACTION_BANNED = "command.join.banned";
    public static final String JOIN_SUCCESS = "command.join.success";
    public static final String JOIN_FULL = "command.join.full";

    public static final String KICK_DESCRIPTION = "command.kick.description";
    public static final String KICK_SUCCESS_SENDER = "command.kick.success.sender";
    public static final String KICK_SUCCESS_RECEIVER = "command.kick.success.receiver";

    public static final String LEAVE_DESCRIPTION = "command.leave.description";
    public static final String LEAVE_SUCCESS = "command.leave.success";
    public static final String LEAVE_OWN_FACTION = "command.leave.error.own-faction";

    public static final String BANNED_LIST_DESCRIPTION = "command.banned_list.description";

    public static final String MAP_DESCRIPTION = "command.map.description";

    public static final String RANK_DESCRIPTION = "command.rank.description";

    public static final String RELOAD_DESCRIPTION = "command.reload.description";

    public static final String SAVE_DESCRIPTION = "command.save.description";

    public static final String RULES_SET_DESCRIPTION = "command.rules.set.description";

    public static final String SETTINGS_DESCRIPTION = "command.settings.description";

    public static final String UNBAN_DESCRIPTION = "command.unban.description";

    public static final String VERSION_DESCRIPTION = "command.version.description";

    public static final String THIS_COMMAND_DOES_NOT_EXIST = "command.not-exist";

    public static final String TUTORIAL_DESCRIPTION = "tutorial";

    public static final String ADMIN_DESCRIPTION = "command.admin.description";

    public static final String ADMIN_DISBAND_DESCRIPTION = "command.admin.disband.description";
    public static final String ADMIN_GPOWER_DESCRIPTION = "command.admin.gpower.description";
    public static final String ADMIN_SAFEZONE_DESCRIPTION = "command.admin.safzone.description";
    public static final String ADMIN_UNCLAIM_DESCRIPTION = "command.admin.unclaim.description";
    public static final String ADMIN_JOIN_DESCRIPTION = "command.admin.joinSilent.description";

    public static List<LangDefaultDataAddon> langDefaultDataAddons = new ArrayList<>();

    private Map<String, String> messages;

    public LangMessage() {
        messages = new HashMap<>();

        messages.put(ADMIN_RANK_DESCRIPTION_LINE_1, "&8Admins have rights");
        messages.put(ADMIN_RANK_DESCRIPTION_LINE_2, "&8to delete the faction");

        messages.put(BANNED_PLAYER_COMMAND_SUCCESS, "&fBanned &e{banned}");
        messages.put(BANNED_PLAYER_COMMAND_LEAVE, "&fYou got banned from &e{faction_displayName}");
        messages.put(BANNED_PLAYER_COMMAND_ALREADY, "&cAlready banned");
        messages.put(BANNED_PLAYER_COMMAND_DESCRIPTION, "Bans people. &6&lWarning:&r &fThis will temporarily disallow the person from joining your faction. &7&lTipp: &fUse &8/f unban&f to remove the temp ban");

        messages.put(RULES_ARE_EMPTY, "&fWow, such empty... There are no rules");
        messages.put(RULES_SET_COMMAND, "&fSuccessfully set the rules");

        messages.put(AUTO_CLAIM_ENABLED, "&a&lEnabled&r &fauto claim");
        messages.put(AUTO_CLAIM_DESCRIPTION, "Automatically claims all chunks you walk in. &a&lToggleable");
        messages.put(AUTO_CLAIM_DISABLED, "&c&lDisabled&r &fauto claim");

        messages.put(AUTO_UNCLAIM_ENABLED, "&a&lEnabled&r &fauto unclaim");
        messages.put(AUTO_UNCLAIM_DESCRIPTION, "Automatically unclaims all chunks you walk in. &a&lToggleable");
        messages.put(AUTO_UNCLAIM_DISABLED, "&c&lDisabled&r &fauto unclaim");

        messages.put(CLAIM_ONE_DESCRIPTION, "Claim single chunk");
        messages.put(CLAIM_ONE_NO_POWER, "§cCan't claim anymore. Your faction's claim (Power - claimed chunks) power smaller or equal to 0");
        messages.put(CLAIM_ONE_SUCCESS, "§aSuccessfully §fclaimed this chunk");
        messages.put(CLAIM_ONE_NOT_CONNECTED, "§cCan't claim a this chunk. It's not connected to the other claims");
        messages.put(CLAIM_ONE_ALREADY_PROPERTY, "§cThis chunk is already your property");
        messages.put(CLAIM_ONE_OWNED_BY_OTHERS, "§cCan't claim this chunk. It owns to another faction");

        messages.put(UNCLAIM_ONE_SOMETHING_WRONG, "§cSomething went wrong while unclaiming. Please try again. If it isn't working, please report this error. Error: Status response is Null (Utils:124)");
        messages.put(UNCLAIM_ONE_SUCCESS, "§aSuccessfully §funclaimed this chunk");
        messages.put(UNCLAIM_ONE_DISCONNECTED, "§cCan't unclaim a this chunk. If you do, it would disconnect other chunks");
        messages.put(UNCLAIM_ONE_ALREADY_WILDNESS, "§cCan't unclaim this chunk. Its already wildness");
        messages.put(UNCLAIM_ONE_NOT_YOUR_PROPERTY, "§cCan't unclaim this chunk. It owns to another faction");

        messages.put(CLAIM_DESCRIPTION, "Claim a chunk for your faction. This will protect it. Use /f claim one or /f claim auto");
        messages.put(UNCLAIM_DESCRIPTION, "Unclaim chunks. &6&l&nWarning: &r&fThis will remove the protection");

        messages.put(RANK_PERMISSIONS_GUI, "Opens the permissions gui");
        messages.put(RANK_SET, "Set the rank for &ePlayer &f to &cRank");
        messages.put(RANK_SET_SUCCESS, "&aSuccessfully &echanged the rank for &e{player} &fto {rank}");

        messages.put(CREATE_DESCRIPTION, "Create a faction");
        messages.put(CREATE_ALREADY_EXISTS, "§cThis faction already exists");
        messages.put(CREATE_NEED_NAME, "§cYou need to name your faction");
        messages.put(CREATE_ALREADY_IN_FACTION, "§cYou can't create a faction, because you are already in one");
        messages.put(CREATE_NO_COLOR_IN_NAME_PERM, "§6You §cdon't have §6enough permissions for colors in faction names");
        messages.put(CREATE_SUCCESS, "§fYou §asuccessfully §fcreated your faction §e{faction_displayname}");
        messages.put(CREATE_CANNOT_JOIN, "§cCouldn't join. {error}");

        messages.put(DELETE_DESCRIPTION, "Delete your faction. &c&lWarning: &r&7This will remove the faction. This cannot be undone");
        messages.put(DELETE_SUCCESS, "§fYou §asuccessfully §fdeleted &e{faction_displayname}");
        messages.put(DELETE_ERROR, "§cCouldn't delete this faction. {error}");

        messages.put(DESCRIPTION_DESCRIPTION, "Set the description users can see when using /f info");
        messages.put(DESCRIPTION_SUCCESS, "§fYou §asuccessfully §fset the description");

        messages.put(HELP_DESCRIPTION, "This command is not finished");

        messages.put(INVITE_DESCRIPTION, "Invite someone into your faction");
        messages.put(INVITE_SUCCESS_SENDER, "&fYou have &er{player_receive} &fan invite to your faction");
        messages.put(INVITE_SUCCESS_RECEIVER, "&fYou have received an invite from &e{faction_displayname}. &n&aClick to join");
        messages.put(INVITE_HOVER_EVENT, "Click to join the faction");

        messages.put(JOIN_DESCRIPTION, "Join a faction. Needs: &8<Faction>");
        messages.put(JOIN_ERROR_FACTION_BANNED, "&cCannot join this faction. You got §c§lBanned");
        messages.put(JOIN_ERROR_FACTION_PRIVATE, "&cCannot join this faction. The faction is private");
        messages.put(JOIN_ERROR_NO_FACTION_FOUND, "§cCouldn't find the faction you were searching for. Maybe a spelling error");
        messages.put(JOIN_FULL, "§cCouldn't join. The faction has no more space for new members");
        messages.put(JOIN_SUCCESS, "§fYou §asuccessfully §fjoined {faction_displayname}");

        messages.put(KICK_DESCRIPTION, "Kicks a player from your faction. &6Warning: &fWith this command, you remove the player from the faction");
        messages.put(KICK_SUCCESS_SENDER, "§fKicked §e{kicked}");
        messages.put(KICK_SUCCESS_RECEIVER, "§cYou were kicked from §e{faction_displayname}");

        messages.put(LEAVE_DESCRIPTION, "§fLeave your current faction");
        messages.put(LEAVE_SUCCESS, "§fYou §asuccessfully §fleft {faction_displayname}");
        messages.put(LEAVE_OWN_FACTION, "§cCannot leave your own faction. Delete it or give someone else owner rights");

        messages.put(BANNED_LIST_DESCRIPTION, "§fLists all banned people in your faction");

        messages.put(MAP_DESCRIPTION, "Displays the claimed chunks around you");

        messages.put(RANK_DESCRIPTION, "Manages the ranks within your faction");

        messages.put(RELOAD_DESCRIPTION, "Reload the configs. &c&lAdmin command. &fNot give anyone permissions for this");

        messages.put(RULES_DESCRIPTION, "Get the rules of your faction");

        messages.put(SAVE_DESCRIPTION, "Save the faction data");

        messages.put(RULES_SET_DESCRIPTION, "Set the rules for your faction");

        messages.put(SETTINGS_DESCRIPTION, "Opens the settings gui");

        messages.put(UNBAN_DESCRIPTION, "&fUnban a player");

        messages.put(VERSION_DESCRIPTION, "Shows the version currently installed");

        messages.put(ADMIN_GPOWER_DESCRIPTION, "§fGive power to a faction");
        messages.put(ADMIN_DISBAND_DESCRIPTION, "§fDelete any faction. §c§lWarning: §r§7This will kick all embers and remove it entirely");
        messages.put(ADMIN_JOIN_DESCRIPTION, "§fJoins &f&lany&r faction silently. §7§lTipp: §7You are leaving your current faction and join any faction without letting anyone know, that you join");
        messages.put(ADMIN_UNCLAIM_DESCRIPTION, "§fUnclaim ANY chunk. Even safezone. &6&lWarning:&f this will remove protection");
        messages.put(ADMIN_SAFEZONE_DESCRIPTION, "§fClaim unclaimable land. Usefull when using worldguard or something else");

        messages.put(THIS_COMMAND_DOES_NOT_EXIST, "&cThis command doesn't exist");

        messages.put(TUTORIAL_DESCRIPTION + ".0", "Welcome to Factions Plugin");
        messages.put(TUTORIAL_DESCRIPTION + ".1", "This is an exciting mode for team battles");
        messages.put(TUTORIAL_DESCRIPTION + ".2", "Alone, it will be quite difficult here, because " +
                "the more players there are in the faction, the more territory you can hold under you!");
        messages.put(TUTORIAL_DESCRIPTION + ".3", "Let's start with the /f help commands, here you can" +
                " see a list of the main commands.");
        messages.put(TUTORIAL_DESCRIPTION + ".4", "Now it's time to create your faction for this type" +
                " /f create *Name of your faction*.");
        messages.put(TUTORIAL_DESCRIPTION + ".5", "Initially, you are given 20 strength at the start." +
                " We hope you didn't die somewhere along the way, but if so write /f power to see how" +
                " much power you have. After all, it will be needed for private chunks.");
        messages.put(TUTORIAL_DESCRIPTION + ".6", "Oh yes, to protect your resources you need private, " +
                "for this write /f claim. You will lock one chunk of 16x16 blocks. The number of " +
                "strength of all players is summed up in the faction, one private is 2 strength. " +
                "Be careful if you lose power, then your enemies will be able to seize your territory!" +
                " Who are at war with you! The meaning of this game is to find Allies and attack " +
                "enemies together! Stock up on dynamite, after all. This is a way to get into the " +
                "territory of your enemies, but be quick, because if they are afraid of you and " +
                "leave the game like miserable rats, you will have 15 minutes to bomb their chests " +
                "and steal resources, because then their lands will become invulnerable!");



        messages.put(INFO_DESCRIPTION, "&fGet a list of all your members + there ranks");

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
}
