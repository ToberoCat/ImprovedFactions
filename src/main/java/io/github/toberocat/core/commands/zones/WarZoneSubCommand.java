package io.github.toberocat.core.commands.zones;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.List;

public class WarZoneSubCommand extends SubCommand {
    public WarZoneSubCommand() {
        super("warzone", "zones.warzone", "command.zones.warzone.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        MainIF.getIF().getClaimManager().protectChunk(ClaimManager.WARZONE_REGISTRY, player.getLocation().getChunk());
        Language.sendMessage("command.zones.warzone.claim", player);
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
