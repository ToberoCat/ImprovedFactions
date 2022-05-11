package io.github.toberocat.core.commands.factions.unclaim;

import io.github.toberocat.core.listeners.PlayerMoveListener;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class UnclaimAutoSubCommand extends SubCommand {

    public UnclaimAutoSubCommand() {
        super("auto", "unclaim.auto", "", false);

    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        UUID id = player.getUniqueId();

        if (PlayerMoveListener.AUTO_CLAIM_OPERATIONS.containsKey(id) &&
                PlayerMoveListener.AUTO_CLAIM_OPERATIONS.get(id) == PlayerMoveListener.ClaimAutoType.UNCLAIM) {
            PlayerMoveListener.AUTO_CLAIM_OPERATIONS.remove(id);
            Language.sendRawMessage("&cDisabled&f auto unclaim", player);
            return;
        }

        Language.sendRawMessage("&aEnabled&f auto unclaim", player);
        PlayerMoveListener.AUTO_CLAIM_OPERATIONS.put(player.getUniqueId(),
                PlayerMoveListener.ClaimAutoType.UNCLAIM);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
