package io.github.toberocat.command;

import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.gui.MapGui;
import org.bukkit.entity.Player;

import java.util.List;

public class ClaimGuiSubCommand extends SubCommand {
    public ClaimGuiSubCommand() {
        super("claim", "command.faction.claim.description", false);
    }

    @Override
    protected void CommandExecute(Player player, String[] strings) {
        AsyncTask.runLaterSync(0, () -> new MapGui(player));
    }

    @Override
    protected List<String> CommandTab(Player player, String[] strings) {
        return null;
    }
}
