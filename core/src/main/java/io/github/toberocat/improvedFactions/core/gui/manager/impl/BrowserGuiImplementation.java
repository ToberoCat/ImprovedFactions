package io.github.toberocat.improvedFactions.core.gui.manager.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.toberocat.improvedFactions.core.gui.manager.GuiManager;
import io.github.toberocat.improvedFactions.core.json.Json;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public interface BrowserGuiImplementation extends GuiImplementation {

    String editorUrl = "https://toberocat.github.io/ImprovedFactions_new/" +
            "website/editor";

    @Override
    default void openEditor(@NotNull CommandSender sender, @NotNull String guiId) {
        if (sender instanceof FactionPlayer<?> player)
            player.sendMessage(translatable -> translatable
                            .getMessages()
                            .getMisc()
                            .get("gui-editor")
                            .get("send-browser-link"),
                    new Placeholder("link", generateLink(guiId))
            );
        else {
            Logger.api().logInfo("Copy & Paste the " +
                    "following link to edit your gui in the browser");
            Logger.api().logInfo(generateLink(guiId));
        }
    }

    private @NotNull String generateLink(@NotNull String guiId) {
        try {
            return editorUrl + "?gui=" + Base64
                    .getEncoder()
                    .encodeToString(
                            Json.parse(GuiManager.getGui(guiId))
                                    .getBytes()
                    );
        } catch (JsonProcessingException e) {
            return editorUrl;
        }
    }
}
