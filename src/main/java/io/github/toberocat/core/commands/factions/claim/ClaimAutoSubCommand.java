package io.github.toberocat.core.commands.factions.claim;

import io.github.toberocat.core.listeners.PlayerMoveListener;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class ClaimAutoSubCommand extends SubCommand {

    public ClaimAutoSubCommand() {
        super("auto", "claim.auto", "", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        UUID id = player.getUniqueId();

        if (PlayerMoveListener.AUTO_CLAIM_OPERATIONS.containsKey(id) &&
                PlayerMoveListener.AUTO_CLAIM_OPERATIONS.get(id) == PlayerMoveListener.ClaimAutoType.CLAIM) {
            PlayerMoveListener.AUTO_CLAIM_OPERATIONS.remove(id);
            Language.sendRawMessage("&cDisabled&f auto claim", player);
            return;
        }

        Language.sendRawMessage("&aEnabled&f auto claim", player);
        PlayerMoveListener.AUTO_CLAIM_OPERATIONS.put(player.getUniqueId(),
                PlayerMoveListener.ClaimAutoType.CLAIM);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
