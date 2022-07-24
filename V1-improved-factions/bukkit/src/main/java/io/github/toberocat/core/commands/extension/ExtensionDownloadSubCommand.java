package io.github.toberocat.core.commands.extension;

import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.extensions.list.ExtensionList;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.gui.extensions.DownloadGUI;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExtensionDownloadSubCommand extends SubCommand {
    public ExtensionDownloadSubCommand() {
        super("download", "extension.download", "command.extension.download.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setUseWhenFrozen(true);
    }

    @Override
    protected void commandExecute(Player player, String[] args) {
        if (args.length == 0) {
            AsyncTask.runLaterSync(0, () -> new DownloadGUI(player));
        } else {
            ExtensionListLoader.getMap().then((map) -> {
                if (!map.containsKey(args[0]))
                    Language.sendRawMessage("Couldn't find extension you where searching for", player);
                else
                    DownloadGUI.downloadExtension(map.get(args[0]), player);
            });
        }
    }

    @Override
    protected List<String> commandTab(Player player, String[] args) {
        return Arrays.stream(ExtensionListLoader.readExtensions().await())
                .map(ExtensionObject::getRegistryName).toList();
    }
}
