package io.github.toberocat.improvedfactions.factions;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.factionCommands.BanSubCommand;
import io.github.toberocat.improvedfactions.commands.factionCommands.InviteSubCommand;
import io.github.toberocat.improvedfactions.commands.factionCommands.KickSubCommand;
import io.github.toberocat.improvedfactions.gui.FactionRanksGui;
import io.github.toberocat.improvedfactions.gui.FactionSettingsGui;
import io.github.toberocat.improvedfactions.gui.Flag;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.ranks.AdminRank;
import io.github.toberocat.improvedfactions.ranks.MemberRank;
import io.github.toberocat.improvedfactions.ranks.OwnerRank;
import io.github.toberocat.improvedfactions.ranks.Rank;
import io.github.toberocat.improvedfactions.utility.SignMenuFactory;
import io.github.toberocat.improvedfactions.utility.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FactionSettings {

    public static Map<String, Flag> FLAGS = new HashMap<>();
    public static Map<String, FactionRankPermission> RANKS = new HashMap<>();

    private Map<String, Flag> flags;
    private Map<String, FactionRankPermission> ranks;

    public static void Init() {
        RANKS.put(Faction.CLAIM_CHUNK_PERMISSION, new FactionRankPermission(Utils.createItem(Material.SHIELD, Language.format("&aClaim chunk"), null), new Rank[] {
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));
        RANKS.put(Faction.UNCLAIM_CHUNK_PERMISSION, new FactionRankPermission(Utils.createItem(Material.WOODEN_AXE, Language.format("&aUnclaim chunk"), null), new Rank[] {
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));
        RANKS.put(Faction.INVITE_PERMISSION, new FactionRankPermission(Utils.createItem(Material.NAME_TAG, Language.format("&aInvite others"), null), new Rank[] {
                Rank.fromString(MemberRank.registry),
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));
        RANKS.put(Faction.BUILD_PERMISSION, new FactionRankPermission(Utils.createItem(Material.BRICKS, Language.format("&aBuild permission"), null), new Rank[]{
                Rank.fromString(MemberRank.registry),
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));
        RANKS.put(Faction.BREAK_PERMISSION, new FactionRankPermission(Utils.createItem(Material.WOODEN_PICKAXE, Language.format("&aBreak permission"), null), new Rank[]{
                Rank.fromString(MemberRank.registry),
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));
        RANKS.put(Faction.LIST_BANNED_PERMISSION, new FactionRankPermission(Utils.createItem(Material.BARRIER, Language.format("&aList banned"), null), new Rank[] {
                Rank.fromString(OwnerRank.registry),
                Rank.fromString(AdminRank.registry)
        }));

        FLAGS.put(Faction.OPENTYPE_FLAG, new Flag(Flag.FlagType.Enum, Material.END_PORTAL_FRAME, "&aOpenType",
                Arrays.asList(Faction.OpenType.values()), Faction.OpenType.Private.ordinal()));

        FLAGS.put(Faction.RENAME_FLAG, new Flag(Flag.FlagType.Function, Material.OAK_SIGN, "&aRename faction",
                "&8Rename your faction", (faction, player, object) -> {
                    try {
                        new AnvilGUI.Builder()
                                .onClose((user) -> {
                                    new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
                                })
                                .onComplete((user, text) -> {
                                    faction.setDisplayName(text);
                                    return AnvilGUI.Response.close();
                                })
                                .text(ChatColor.stripColor(faction.getDisplayName()))
                                .itemLeft(new ItemStack(Material.GRAY_BANNER))
                                .title("§eChange your display name")
                                .plugin(ImprovedFactionsMain.getPlugin())
                                .open(player);
                    } catch (Exception e) {
                        player.sendMessage("Please use §8/f settings rename");
                    }
                }));
        FLAGS.put(Faction.MOTD, new Flag(Flag.FlagType.Function, Material.BIRCH_SIGN, "&aFaction motd",
                "&8Set your faction motd", (faction, player, object) -> {
                    try {
                        new AnvilGUI.Builder()
                                .onClose((user) -> new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction))
                                .onComplete((user, text) -> {
                                    faction.setMotd(Language.format(text));
                                    return AnvilGUI.Response.close();
                                })
                                .text(ChatColor.stripColor(faction.getMotd()))
                                .itemLeft(new ItemStack(Material.BLACK_BANNER))
                                .title("§eChange your motd name")
                                .plugin(ImprovedFactionsMain.getPlugin())
                                .open(player);
                    } catch (Exception e) {
                        player.sendMessage("Please use §8/f settings motd");
                    }
                }));

        FLAGS.put("OPEN_RANK", new Flag(Flag.FlagType.Function, Material.BLUE_DYE, "&aRanks",
                        "&8Configure your faction rank permissions", ((faction, player, object) -> {
            new FactionRanksGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
        })));

        FLAGS.put("INVITE", new Flag(Flag.FlagType.Function, Material.GREEN_BANNER, "&aInvite",
                "&8Invite a player", ((faction, player, object) -> {
            new AnvilGUI.Builder()
                    .onClose((user) -> {
                        new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
                    })
                    .onComplete((user, text) -> {
                        InviteSubCommand.invite(user, Bukkit.getPlayer(text));
                        return AnvilGUI.Response.close();
                    })
                    .text("Player name")
                    .itemLeft(new ItemStack(Material.GREEN_BANNER))
                    .title("§eInvite player")
                    .plugin(ImprovedFactionsMain.getPlugin())
                    .open(player);
                })));
        FLAGS.put("BAN", new Flag(Flag.FlagType.Function, Material.RED_BANNER, "&aBan",
                "&8Ban a player", ((faction, player, object) -> {
            new AnvilGUI.Builder()
                    .onClose((user) -> {
                        new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
                    })
                    .onComplete((user, text) -> {
                        BanSubCommand.ban(user, Bukkit.getOfflinePlayer(text));
                        return AnvilGUI.Response.close();
                    })
                    .text("Member name")
                    .itemLeft(new ItemStack(Material.RED_BANNER))
                    .title("§eBan player")
                    .plugin(ImprovedFactionsMain.getPlugin())
                    .open(player);
            })));

        FLAGS.put("KICK", new Flag(Flag.FlagType.Function, Material.ORANGE_BANNER, "&aKick",
                "&8Kick a player", ((faction, player, object) -> {
            new AnvilGUI.Builder()
                    .onClose((user) -> {
                        new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
                    })
                    .onComplete((user, text) -> {
                        KickSubCommand.kick(user, Bukkit.getOfflinePlayer(text));
                        return AnvilGUI.Response.close();
                    })
                    .text("Member name")
                    .itemLeft(new ItemStack(Material.ORANGE_BANNER))
                    .title("§eKick player")
                    .plugin(ImprovedFactionsMain.getPlugin())
                    .open(player);
        })));

        FLAGS.put("DESCRIPTION", new Flag(Flag.FlagType.Function, Material.BLUE_BANNER, "&aDescription",
                "&8Change your faction description. &7Tipp: &8Use colorcodes", ((faction, player, object) -> {
            new AnvilGUI.Builder()
                    .onClose((user) -> {
                        new FactionSettingsGui(player, ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction);
                    })
                    .onComplete((user, text) -> {
                        faction.setDescription(Language.format(text));
                        return AnvilGUI.Response.close();
                    })
                    .text(faction.getDescription())
                    .itemLeft(new ItemStack(Material.BLUE_BANNER))
                    .title("§eChange description")
                    .plugin(ImprovedFactionsMain.getPlugin())
                    .open(player);
        })));

        for (String key : FLAGS.keySet()) {
            ImprovedFactionsMain.getPlugin().getConfig().addDefault("factions.flags." + key, true);
        }
        for (String key : RANKS.keySet()) {
            ImprovedFactionsMain.getPlugin().getConfig().addDefault("factions.permissions." + key, true);
        }

        ImprovedFactionsMain.getPlugin().getConfig().options().copyDefaults(true);
        ImprovedFactionsMain.getPlugin().saveConfig();
    }

    public FactionSettings() {
        flags = new HashMap<>();
        ranks = new HashMap<>();

        FileConfiguration config = ImprovedFactionsMain.getPlugin().getConfig();

        for (String key : FLAGS.keySet()) {
            Flag flag = FLAGS.get(key);
            if (config.getBoolean("factions.flags." + key)) {
                flags.put(key, flag);
            }
        }
        for (String key : RANKS.keySet()) {
            FactionRankPermission permission = RANKS.get(key);
            if (config.getBoolean("factions.permissions." + key)) {
                ranks.put(key, permission);
            }
        }
    }

    public Map<String, Flag> getFlags() {
        return flags;
    }

    public Map<String, FactionRankPermission> getRanks() {
        return ranks;
    }
}
