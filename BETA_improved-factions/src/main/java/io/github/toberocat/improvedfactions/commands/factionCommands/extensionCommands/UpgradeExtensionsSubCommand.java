package io.github.toberocat.improvedfactions.commands.factionCommands.extensionCommands;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.commands.subCommands.SubCommand;
import io.github.toberocat.improvedfactions.extentions.ExtensionDownloadCallback;
import io.github.toberocat.improvedfactions.extentions.ExtensionDownloader;
import io.github.toberocat.improvedfactions.extentions.ExtensionObject;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionListLoader;
import io.github.toberocat.improvedfactions.language.Language;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class UpgradeExtensionsSubCommand extends SubCommand {
    public UpgradeExtensionsSubCommand() {
        super("upgrade", "This will install the latest versions of the installed extensions");
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> keys = new ArrayList<>(ImprovedFactionsMain.extensions.keySet());

                final boolean[] nextExtension = {true};
                int i = 0;
                while (i < keys.size()) {
                    if (nextExtension[0]) {
                        nextExtension[0] = false;
                        try {
                            ExtensionDownloader.DownloadExtension(ExtensionListLoader.getExtenionObject(keys.get(i)),
                                    ImprovedFactionsMain.getPlugin().getDataFolder().getPath() + "/Extensions", new ExtensionDownloadCallback() {
                                        @Override
                                        public void StartDownload(ExtensionObject extension) {
                                            player.sendMessage(Language.getPrefix() + "§fStarting download for §e" + extension.getName());
                                        }

                                        @Override
                                        public void FinishedDownload(ExtensionObject extension) {
                                            player.sendMessage(Language.getPrefix() + "§fFinished download for §e" + extension.getName());
                                            nextExtension[0] = true;
                                        }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
                player.sendMessage(Language.getPrefix() + "§fFinished upgrading extensions. To enable, please use §7/rl");
            }
        }).run();
    }

    @Override
    protected List<String> CommandTab(Player player, String[] args) {
        return null;
    }
}
