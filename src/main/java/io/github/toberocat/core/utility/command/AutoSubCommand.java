package io.github.toberocat.core.utility.command;

import io.github.toberocat.core.listeners.PlayerMoveListener;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class AutoSubCommand extends SubCommand {
    private UUID id = null;
    public AutoSubCommand(String subCommand, String permission, String descriptionKey, boolean manager) {
        super(subCommand, permission, descriptionKey, manager);
    }

    public AutoSubCommand(String subCommand, String descriptionKey, boolean manager) {
        super(subCommand, descriptionKey, manager);
    }

    public abstract String getEnabledKey();
    public abstract String getDisabledKey();

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 0) {
            onSingle(player);
        } else {
            if (args[0].equals("one")) {
                onSingle(player);
            } else if (args[0].equals("auto")) {
                if (id == null) {
                    id = UUID.randomUUID();
                    if (!PlayerMoveListener.MOVE_OPERATIONS.containsKey(player.getUniqueId())) {
                        PlayerMoveListener.MOVE_OPERATIONS.put(player.getUniqueId(), new HashMap<>());
                    }

                    PlayerMoveListener.MOVE_OPERATIONS.get(player.getUniqueId()).put(id.toString(), this::onSingle);
                    Language.sendMessage(getEnabledKey(), player);
                    onSingle(player);
                } else {
                    if (!PlayerMoveListener.MOVE_OPERATIONS.containsKey(player.getUniqueId())) {
                        Language.sendMessage(getDisabledKey(), player);
                        return;
                    }

                    PlayerMoveListener.MOVE_OPERATIONS.get(player.getUniqueId()).remove(id.toString());
                    if (PlayerMoveListener.MOVE_OPERATIONS.get(player.getUniqueId()).size() == 0)
                        PlayerMoveListener.MOVE_OPERATIONS.remove(player.getUniqueId());

                    Language.sendMessage(getDisabledKey(), player);
                    id = null;
                }
            }
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return List.of("one", "auto");
    }


    public abstract void onSingle(Player player);
}
