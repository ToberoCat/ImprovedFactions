package io.github.toberocat.improvedFactions.core.translator;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.handler.ConfigHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class Translation {

    public static final String DEFAULT_LANG_FILE = ConfigHandler.api()
            .getString("language.default-file", "en_us");

    private static final Map<String, Translatable> TRANSLATABLE_MAP = new HashMap<>();
    private static final Map<UUID, String> LANGUAGE_LOCALE_USAGE = new HashMap<>();
    private static final Map<String, String> LOCALE_TO_FILE_MAP = new HashMap<>();

    private static final FileAccess ACCESS = new FileAccess(ImprovedFactions.api().getLangFolder());

    private final UUID playerId;

    /* Static manager methods */
    public static void createLocaleMap() throws IOException {
        for (File file : ACCESS.listFiles())
            XmlLoader.read(Translatable.class, file)
                    .getMeta()
                    .getLanguages()
                    .forEach(x -> LOCALE_TO_FILE_MAP.put(x, file.getName()));
    }

    public static void playerJoin(@NotNull FactionPlayer<?> player) {
        String local = player.getLocal();
        if (TRANSLATABLE_MAP.containsKey(local)) {
            LANGUAGE_LOCALE_USAGE.put(player.getUniqueId(), local);
            return;
        }
        /* Load language file */
        Translatable translation = readFile(local);
        if (translation == null) {
            LANGUAGE_LOCALE_USAGE.put(player.getUniqueId(), DEFAULT_LANG_FILE);
            return;
        }

        TRANSLATABLE_MAP.put(local, translation);
        LANGUAGE_LOCALE_USAGE.put(player.getUniqueId(), local);
    }

    public static void playerLeave(@NotNull FactionPlayer<?> player) {
        String local = player.getLocal();
        LANGUAGE_LOCALE_USAGE.remove(player.getUniqueId());

        if (LANGUAGE_LOCALE_USAGE.values()
                .stream()
                .anyMatch(x -> x.equals(local))) return;

        TRANSLATABLE_MAP.remove(local);
    }

    public static void dispose() {
        TRANSLATABLE_MAP.clear();
        LANGUAGE_LOCALE_USAGE.clear();
        LOCALE_TO_FILE_MAP.clear();
    }

    private static @Nullable Translatable readFile(@NotNull String locale) {
        String file = LOCALE_TO_FILE_MAP.get(locale);
        if (file == null) return null;

        File langFile = ACCESS.getFile(file);
        if (!langFile.exists()) return null;

        try {
            return XmlLoader.read(Translatable.class, langFile);
        } catch (IOException e) {
            return null;
        }
    }

    public Translation(UUID playerId) {
        this.playerId = playerId;
    }

    public @Nullable String getMessage(@NotNull Function<Translatable, String> query) {
        String locale = LANGUAGE_LOCALE_USAGE.get(playerId);
        Translatable translatable = TRANSLATABLE_MAP.get(locale);
        if (translatable == null) return null;

        return query.apply(translatable);
    }

    public @Nullable String[] getMessages(@NotNull Function<Translatable, String[]> query) {
        String locale = LANGUAGE_LOCALE_USAGE.get(playerId);
        Translatable translatable = TRANSLATABLE_MAP.get(locale);
        if (translatable == null) return null;

        return query.apply(translatable);
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
