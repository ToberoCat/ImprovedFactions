package io.github.toberocat.improvedfactions.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.event.FactionEvent;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionUtils;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.utility.callbacks.ExceptionCallback;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Utils {
    public static ItemStack createItem(final Material material, final String name, final String[] lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        if (lore != null) meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack modiflyItem(ItemStack stack, String title, String... lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(Arrays.asList(lore));
        ItemStack item = new ItemStack(stack);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack getSkull(String url, int count, String name, String... lore) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, count);
        if(url.isEmpty())return head;


        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            assert headMeta != null;
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);

        ItemMeta meta = head.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) meta.setLore(Arrays.asList(lore));
        head.setItemMeta(meta);
        return head;
    }

    public static <T> List<String> listToStringList(List<T> list) {
        List<String> strList = new ArrayList<>();
        for (T type : list) {
            strList.add(type.toString());
        }
        return strList;
    }

    public static void ClaimChunk(Player player) {
        if (FactionUtils.getFaction(player) != null) {
            Chunk chunk = player.getLocation().getChunk();
            Faction faction = ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction;

            faction.ClaimChunk(chunk, status -> {
                if (status == null) { //No power left
                    Language.sendMessage(LangMessage.CLAIM_ONE_NO_POWER, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_ALLOWED_WORLD) {
                    player.sendMessage(Language.getPrefix() + "§cCannot claim in this world");
                } else if (status.getClaimStatus() == ClaimStatus.Status.SUCCESS) { //Claimed chunk successfully
                    Language.sendMessage(LangMessage.CLAIM_ONE_SUCCESS, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NEED_CONNECTION) { //Chunk needs to be connected
                    Language.sendMessage(LangMessage.CLAIM_ONE_NOT_CONNECTED, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.ALREADY_CLAIMED) { //Chunk claimed by another faction
                    if (status.getFactionClaim().getRegistryName().equals(faction.getRegistryName())) {
                        Language.sendMessage(LangMessage.CLAIM_ONE_ALREADY_PROPERTY, player);
                    } else {
                        Language.sendMessage(LangMessage.CLAIM_ONE_OWNED_BY_OTHERS, player);
                    }
                } else {
                    player.sendMessage(Language.getPrefix() + "§cError: "  + status.getClaimStatus().toString());
                }
            });
        } else {
            player.sendMessage(Language.getPrefix() + "§cYou need to be in a faction");
        }
    }

    public static void ClaimChunk(Faction faction, Player player) {
        if (player == null) return;

        Chunk chunk = player.getLocation().getChunk();

        faction.ClaimChunk(chunk, status -> {
            if (status == null) { //No power left
                Language.sendMessage(LangMessage.CLAIM_ONE_NO_POWER, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_ALLOWED_WORLD) {
                player.sendMessage(Language.getPrefix() + "§cCannot claim in this world");
            } else if (status.getClaimStatus() == ClaimStatus.Status.SUCCESS) { //Claimed chunk successfully
                Language.sendMessage(LangMessage.CLAIM_ONE_SUCCESS, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NEED_CONNECTION) { //Chunk needs to be connected
                Language.sendMessage(LangMessage.CLAIM_ONE_NOT_CONNECTED, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.ALREADY_CLAIMED) { //Chunk claimed by another faction
                if (status.getFactionClaim().getRegistryName().equals(faction.getRegistryName())) {
                    Language.sendMessage(LangMessage.CLAIM_ONE_ALREADY_PROPERTY, player);
                } else {
                    Language.sendMessage(LangMessage.CLAIM_ONE_OWNED_BY_OTHERS, player);
                }
            } else {
                player.sendMessage(Language.getPrefix() + "§cError: "  + status.getClaimStatus().toString());
            }
        });
    }

    public static void UnClaimChunk(Player player) {
        if (FactionUtils.getFaction(player) != null) {
            Chunk chunk = player.getLocation().getChunk();
            Faction faction = ImprovedFactionsMain.playerData.get(player.getUniqueId()).playerFaction;

            faction.UnClaimChunk(chunk, status -> {
                if (status == null) { //No power left
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_SOMETHING_WRONG, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_ALLOWED_WORLD) {
                    player.sendMessage(Language.getPrefix() + "§cCannot unclaim in this world");
                } else if (status.getClaimStatus() == ClaimStatus.Status.SUCCESS) { //Claimed chunk successfully
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_SUCCESS, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NEED_CONNECTION) { //Chunk needs to be connected
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_DISCONNECTED, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_CLAIMED) {
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_ALREADY_WILDNESS, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_PROPERTY) {
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_NOT_YOUR_PROPERTY, player);
                } else if (status.getClaimStatus() == ClaimStatus.Status.ALREADY_CLAIMED) { //Chunk claimed by another faction
                    if (status.getFactionClaim().getRegistryName().equals(faction.getRegistryName())) {
                        player.sendMessage(Language.getPrefix() + "§cThis chunk is already your property");
                    } else {
                        Language.sendMessage(LangMessage.UNCLAIM_ONE_NOT_YOUR_PROPERTY, player);
                    }
                }
            });
        } else {
            player.sendMessage(Language.getPrefix() + "§cYou need to be in a faction");
        }
    }

    public static void UnClaimChunk(Faction faction, Player player) {
        Chunk chunk = player.getLocation().getChunk();
        faction.UnClaimChunk(chunk, status -> {
            if (status == null) { //No power left
                Language.sendMessage(LangMessage.UNCLAIM_ONE_SOMETHING_WRONG, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_ALLOWED_WORLD) {
                player.sendMessage(Language.getPrefix() + "§cCannot unclaim in this world");
            } else if (status.getClaimStatus() == ClaimStatus.Status.SUCCESS) { //Claimed chunk successfully
                Language.sendMessage(LangMessage.UNCLAIM_ONE_SUCCESS, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NEED_CONNECTION) { //Chunk needs to be connected
                Language.sendMessage(LangMessage.UNCLAIM_ONE_DISCONNECTED, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_CLAIMED) {
                Language.sendMessage(LangMessage.UNCLAIM_ONE_ALREADY_WILDNESS, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.NOT_PROPERTY) {
                Language.sendMessage(LangMessage.UNCLAIM_ONE_NOT_YOUR_PROPERTY, player);
            } else if (status.getClaimStatus() == ClaimStatus.Status.ALREADY_CLAIMED) { //Chunk claimed by another faction
                if (status.getFactionClaim().getRegistryName().equals(faction.getRegistryName())) {
                    player.sendMessage(Language.getPrefix() + "§cThis chunk is already your property");
                } else {
                    Language.sendMessage(LangMessage.UNCLAIM_ONE_NOT_YOUR_PROPERTY, player);
                }
            }
        });

    }

    public static void run(ExceptionCallback cb) {
        cb.callback();
    }


    public static void except(Exception e) {
        e.printStackTrace();
    }

    public static boolean CallEvent(Class<FactionEvent> eventClazz, Faction faction, Object[] objects, boolean cancellable)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        FactionEvent event = eventClazz.getConstructor(Faction.class, Object[].class).newInstance(faction, objects);
        Bukkit.getPluginManager().callEvent(event);

        return !cancellable || !event.isCancelled();
    }

}
