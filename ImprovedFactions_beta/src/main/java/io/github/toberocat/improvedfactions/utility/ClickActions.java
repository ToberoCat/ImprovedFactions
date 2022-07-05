package io.github.toberocat.improvedfactions.utility;

import io.github.toberocat.improvedfactions.commands.FactionCommand;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Class to send json messages to players
 * which can execute an action when clicked
 *
 * @author Mas281
 * @version 1.0
 * @since 3/04/2017
 */
public class ClickActions implements Listener {

    /**
     * Private constructor
     * No new instances of the class
     * are needed to be created outside
     */
    private ClickActions()
    {
        actionMap = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * The instance of the main class
     */
    private static Plugin plugin;

    /**
     * Sets the instance of the plugin
     * @param pluginInstance The plugin instance
     */
    public static void init(Plugin pluginInstance)
    {
        plugin = pluginInstance;
    }

    /**
     * The singleton instance of the class
     */
    private static ClickActions instance;

    /**
     * Gets the instance of the class
     * @return The class instance
     */
    public static ClickActions getInstance()
    {
        return instance == null ? (instance = new ClickActions()) : instance;
    }

    ////////////////////////////////////////////////////

    /**
     * Functional interface for
     * executing actions with
     * a player
     */
    @FunctionalInterface
    public interface PlayerAction
    {
        /**
         * Executes the desired action
         * on a player based upon implementation
         * @param player The player to run the action for
         */
        void run(Player player);
    }

    /**
     * Class holding information about an action
     */
    private class ActionData
    {
        /**
         * The player to execute the action on
         */
        private UUID playerId;

        /**
         * The action to execute
         */
        private PlayerAction action;

        /**
         * Whether the action should expire
         */
        private boolean expire;

        /**
         * ActionData constructor
         * @param playerId The {@link UUID} of the player to execute the action on
         * @param action The {@link PlayerAction} to execute
         */
        private ActionData(UUID playerId, PlayerAction action, boolean expire)
        {
            this.playerId = playerId;
            this.action = action;
            this.expire = expire;
        }

        /**
         * Gets the UUID of the player to execute the action upon
         * @return The stored {@link UUID}
         */
        private UUID getPlayerId()
        {
            return playerId;
        }

        /**
         * Gets the action associated with this ActionData object
         * @return The stored {@link PlayerAction}
         */
        private PlayerAction getAction()
        {
            return action;
        }

        /**
         * Whether the action should expire after being used once
         * @return Whether the action should expire
         */
        private boolean shouldExpire()
        {
            return expire;
        }
    }

    /**
     * Map linking an action UUID and the action
     */
    private Map<UUID, ActionData> actionMap;

    /**
     * Sends a clickable action message to a player
     * @param player The player to send the message to
     * @param msg The message to send to the player
     * @param expire Whether the action should expire after being used once
     * @param action The action to execute when the player clicks the message
     */
    public void sendActionMessage(Player player, String msg, boolean expire, PlayerAction action)
    {
        sendActionMessage(player, new TextComponent(msg), expire, action);
    }

    /**
     * Sends a clickable action message to a player
     * @param player The player to send the message to
     * @param component The text component to send to the player
     * @param expire Whether the action should expire after being used once
     * @param action The action to execute when the player clicks the message
     */
    public void sendActionMessage(Player player, TextComponent component, boolean expire, PlayerAction action)
    {
        sendActionMessage(player, new TextComponent[] {component}, expire, action);
    }

    /**
     * Sends clickable action messages to a player
     * @param player The player to send the message to
     * @param components The text components to send to the player
     * @param expire Whether the action should expire after being used once
     * @param action The action to execute when the player clicks the message
     */
    public void sendActionMessage(Player player, TextComponent[] components, boolean expire, PlayerAction action)
    {
        Validate.notNull(player, "Player cannot be null");
        Validate.notNull(components, "Components cannot be null");
        Validate.notNull(action, "Action cannot be null");

        UUID id = UUID.randomUUID();

        while (actionMap.keySet().contains(id))
        {
            id = UUID.randomUUID();
        }

        actionMap.put(id, new ActionData(player.getUniqueId(), action, expire));

        FactionCommand.subCommands.add(new SubCommand(id.toString(), "") {
            @Override
            protected void CommandExecute(Player player, String[] args) {
                action.run(player);
            }

            @Override
            protected List<String> CommandTab(Player player, String[] args) {
                return null;
            }
        });

        for (BaseComponent component : components)
        {
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f" + id.toString()));
        }

        player.spigot().sendMessage(components);
    }

    /**
     * Remove all the action messages associated with a player
     * @param player The player who's actions should be removed
     */
    public void removeActionMessages(Player player)
    {
        for (Map.Entry<UUID, ActionData> entry : actionMap.entrySet())
        {
            if (entry.getValue().getPlayerId().equals(player.getUniqueId()))
            {
                FactionCommand.subCommands.remove(entry.getKey());
                actionMap.remove(entry.getKey());
            }
        }
    }

    /* Listeners */
    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        removeActionMessages(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        // The command entered
        String command = event.getMessage().split(" ")[0].substring(1);

        UUID id;

        try
        {
            id = UUID.fromString(command);
        }
        catch (IllegalArgumentException expected)
        {
            // They didn't enter a valid UUID
            return;
        }

        // The data associated with the UUID they entered
        ActionData data = actionMap.get(id);

        if (data == null)
        {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (player.getUniqueId().equals(id))
        {
            // They entered a command linked with their data
            data.getAction().run(player);

            // This action should expire after being used once
            if (data.shouldExpire())
            {
                actionMap.remove(id);
            }
        }
    }
}

