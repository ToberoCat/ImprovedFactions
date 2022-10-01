package io.github.toberocat.improvedFactions.core.registry;

import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.gui.GuiImplementation;
import io.github.toberocat.improvedFactions.core.handler.*;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedFactions.core.translator.TranslationFixer;
import io.github.toberocat.improvedFactions.core.translator.XmlManager;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import io.github.toberocat.improvedFactions.core.utils.FileUtils;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

/**
 * These class contains all interfaces that require to get populated when creating a new implementation
 */
public class ImplementationHolder {
    public static @Nullable ConfigHandler configHandler;
    public static @Nullable MessageHandler messageHandler;
    public static @Nullable RankHolder rankHolder;
    public static @Nullable ItemHandler itemHandler;
    public static @Nullable MessagingHandler messagingHandler;
    public static @Nullable Logger logger;
    public static @Nullable GuiImplementation guiImplementation;
    public static @Nullable SoundHandler soundHandler;
    public static @Nullable ActionHandler actionHandler;

    public static @Nullable ImprovedFactions<?> improvedFactions;

    /**
     * This should get called when all core registers should get called
     */
    public static void register() throws IOException, URISyntaxException {
        createFolders();
        copyResources();

        Rank.register();
        ClaimHandler.cacheAllWorlds();
        Translation.createLocaleMap();

        reload();
    }

    public static void reload() { // Reload the data from config
        ClaimHandler.reload();
    }

    private static void createFolders() {
        File file = ImprovedFactions.api().getDataFolder();
        List.of("lang", "guis", "commands", "actions",
                        "data/chunks",
                        "data/factions",
                        "data/persistent",
                        "data/players",
                        "data/messages")
                .forEach(x -> new File(file, x).mkdirs());
    }

    private static void copyResources() throws IOException, URISyntaxException {
        copyLang("en_us.xml");
        CUtils.copyResource("config.yml");
        copyAll("actions");
        copyAll("guis");
    }

    private static void copyAll(@NotNull String root) throws IOException, URISyntaxException {
        FileUtils.list(root).forEach(x -> {
            if (x.toString().equals(root)) return;
            try {
                String file = root + "/" + x.getFileName();
                if (Files.isDirectory(x)) copyAll(file);
                else CUtils.copyResource(file);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private static void copyLang(@NotNull String name) throws IOException {
        String res = "lang/" + name;

        File externalFile = new File(ImprovedFactions.api().getLangFolder().getAbsolutePath(), name);
        if (externalFile.exists()) {
            URL localFile = ImplementationHolder.class.getResource(res);
            if (localFile == null) return;

            Translatable local = XmlManager.read(Translatable.class, localFile);
            Translatable external = XmlManager.read(Translatable.class, externalFile);

            new TranslationFixer(local, external).fix();
            XmlManager.write(external, externalFile);
        }

        CUtils.copyResource(res);
    }

    /**
     * Call it when you don't need any of the core features / want to reload them using a sequence,
     * like:
     * <p>
     * // Reload all handlers
     * dispose();
     * register();
     */
    public static void dispose() {
        ClaimHandler.dispose();
        Translation.dispose();
        PersistentHandler.api().dispose();
        FactionHandler.dispose();
    }

    public static void playerLeave(@NotNull FactionPlayer<?> player) {
        Translation.playerLeave(player);
        PersistentHandler.api().quit(player);
    }
}
