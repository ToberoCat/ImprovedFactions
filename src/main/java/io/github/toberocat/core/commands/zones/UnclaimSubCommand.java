package io.github.toberocat.core.commands.zones;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import org.bukkit.entity.Player;

import java.util.List;

public class UnclaimSubCommand extends SubCommand {
    public UnclaimSubCommand() {
        super("unclaim", LangMessage.COMMAND_ZONES_UNCLAIM_DESCRIPTION, false);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        Result result = MainIF.getIF().getClaimManager().removeProtection(player.getLocation().getChunk());
        Language.sendMessage(LangMessage.COMMAND_ZONES_UNCLAIM, player,
                new Parseable("{chunktype}", (String)result.getPaired()));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
