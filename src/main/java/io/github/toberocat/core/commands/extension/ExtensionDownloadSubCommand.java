package io.github.toberocat.core.commands.extension;

import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.gui.extensions.DownloadGUI;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ExtensionDownloadSubCommand extends SubCommand {
    public ExtensionDownloadSubCommand() {
        super("download", "extension.download", "command.extension.download.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings().setArgLength(0).setUseWhenFrozen(true);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 0) {
            AsyncTask.runLaterSync(0, () -> {
                new DownloadGUI(player);
            });
        } else {
            ExtensionObject[] extensions = ExtensionListLoader.readListSync();
            ExtensionObject[] filtered = Arrays.stream(extensions).filter(x -> x.getRegistryName()
                    .equals(args[0])).toArray(ExtensionObject[]::new);
            if (filtered.length > 0) DownloadGUI.downloadExtension(filtered[0], player, true);
            else Language.sendRawMessage("Couldn't find extension you where searching for", player);
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return Arrays.stream(ExtensionListLoader.readListSync()).map(ExtensionObject::getRegistryName).toList();
    }
}
