package io.github.toberocat.command;

import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.gui.MapGui;
import org.bukkit.entity.Player;

import java.util.List;

public class MapGuiSubCommand extends SubCommand {
    public MapGuiSubCommand() {
        super("map", "command.map.description", false);
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
