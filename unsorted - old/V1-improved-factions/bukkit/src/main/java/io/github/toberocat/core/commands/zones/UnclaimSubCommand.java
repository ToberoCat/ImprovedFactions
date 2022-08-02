package io.github.toberocat.core.commands.zones;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.auto.AutoSubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

public class UnclaimSubCommand extends AutoSubCommand {
    public UnclaimSubCommand() {
        super("unclaim", "zones.unclaim",
                "command.zones.unclaim.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }


    @Override
    public String getEnabledKey() {
        return "command.zones.unclaim.auto-enabled";
    }

    @Override
    public String getDisabledKey() {
        return "command.zones.unclaim.auto-disabled";
    }

    @Override
    public void onSingle(Player player) {
        Result result = MainIF.getIF().getClaimManager().removeProtection(player.getLocation().getChunk());

        if (result.isSuccess()) Language.sendMessage("command.zones.unclaim", player,
                new Parseable("{chunktype}", (String) result.getPaired()));
        else Language.sendRawMessage(result.getPlayerMessage(), player);
    }
}
