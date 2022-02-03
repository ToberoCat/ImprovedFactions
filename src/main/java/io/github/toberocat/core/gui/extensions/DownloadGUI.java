package io.github.toberocat.core.gui.extensions;

import io.github.toberocat.core.extensions.ExtensionDownloadCallback;
import io.github.toberocat.core.extensions.ExtensionDownloader;
import io.github.toberocat.core.extensions.ExtensionObject;
import io.github.toberocat.core.extensions.list.ExtensionListLoader;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.gui.GUISettings;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DownloadGUI extends Gui {
    public DownloadGUI(Player player) {
        super(player, createInventory(player), new GUISettings());
        Language.sendMessage("Downloading extension registry from the internet. Please wait", player);
        AsyncCore.Run(() -> {
            ExtensionListLoader.loadList((extensions) ->{
                Language.sendRawMessage("Finished downloading. Will now list extensions", player);

                for (ExtensionObject extension : extensions) {
                    List<String> loreList = new ArrayList<>();
                    loreList.add("§7Made by: §d" + extension.getAuthor());
                    loreList.add("");
                    loreList.addAll(Utility.wrapLines(extension.getDescription(), "§7"));

                    AddSlot(Utility.createItem(extension.getGuiIcon(), "§e" +
                            extension.getDisplayName(), loreList.toArray(String[]::new)), () -> {
                        try {
                            ExtensionDownloader.DownloadExtension(extension, new ExtensionDownloadCallback() {
                                @Override
                                public void startDownload(ExtensionObject extension) {
                                    Language.sendRawMessage("Started extension download. Don't restart the server!", player);
                                }

                                @Override
                                public void finishedDownload(ExtensionObject extension) {
                                    Language.sendRawMessage("&a&lInstalled&f extension. Reload the server to enable", player);
                                }
                            });
                        } catch (Exception e) {
                            Utility.except(e);
                            player.closeInventory();
                            Language.sendRawMessage("&cCouldn't download extension. " +e.getMessage(), player);
                        }
                    });
                }
            });
        });
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event, Iterator<Gui> iterator) {
        super.onInventoryClose(event, iterator);
        ExtensionListLoader.unloadList();
    }

    private static Inventory createInventory(Player player) {
        return Bukkit.createInventory(player, 56,
                Language.getMessage(LangMessage.GUI_EXTENSION_DOWNLOAD_TITLE, player));
    }
}
