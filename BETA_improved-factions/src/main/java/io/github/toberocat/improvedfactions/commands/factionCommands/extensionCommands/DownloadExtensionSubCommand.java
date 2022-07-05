package io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.extentions.Extension;
import io.github.toberocat.improvedfactions.extentions.ExtensionDownloadCallback;
import io.github.toberocat.improvedfactions.extentions.ExtensionDownloader;
import io.github.toberocat.improvedfactions.extentions.ExtensionObject;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionList;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionListLoader;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownloadExtensionSubCommand extends SubCommand {
    public DownloadExtensionSubCommand() {
        super("download", "extension.download", "&fDownload a extension.");
    }

    private ExtensionObject lastExtensionObj = null;

    @Override
    protected void CommandExecute(Player player, String[] args) {
        if (args.length == 1) {
            for (ExtensionObject extension : ExtensionListLoader.ReadList().getExtensionObjects()) {
                if (extension.getName().equalsIgnoreCase(args[0])) {
                    try {
                        ExtensionDownloader.DownloadExtension(extension,
                                ImprovedFactionsMain.getPlugin().getDataFolder().getPath() + "/Extensions", new ExtensionDownloadCallback() {
                                    @Override
                                    public void StartDownload(ExtensionObject extension) {
                                        player.sendMessage(Language.getPrefix() + "§fStarting download for §e" + extension.getName());
                                    }

                                    @Override
                                    public void FinishedDownload(ExtensionObject extension) {
                                        player.sendMessage(Language.getPrefix() + "§fFinished download for §e" + extension.getName() + ". §fTo enable it, reload the server using /rl. §8Please notice:§f It's recommended to §cdelete §fthe old language files to get the new localization messages");
                                    }
                                });
                    } catch (IOException e) {
                        player.sendMessage(Language.getPrefix() + "§cFailed to download §e" + extension.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        List<String> arguments = new ArrayList<String>();
        if (args.length != 1) return null;
        for (ExtensionObject _extension : ExtensionListLoader.ReadList().getExtensionObjects()) {
            if (!ImprovedFactionsMain.extensions.containsKey(_extension.getName()))
                arguments.add(_extension.getName().replace(" ", "_"));
        }

        ExtensionObject extension = null;
        List<String> results = new ArrayList<String>();
        for (String arg : args) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(arg.toLowerCase())) {
                    results.add(a);
                    extension = getObjectFromList(a);
                }
            }
        }
        if (results.size() == 1) {
            if (extension != null) {
                if (lastExtensionObj != extension) {
                    player.sendMessage("§e§l"+extension.getDisplayName() + ": §f" + extension.getDescription());
                    lastExtensionObj = extension;
                }
            }
        }

        return arguments;
    }

    private ExtensionObject getObjectFromList(String name) {
        for (ExtensionObject objects : ExtensionListLoader.ReadList().getExtensionObjects()) {
            if (objects.getName().equals(name)) {
                return objects;
            }
        }
        return null;
    }
}
