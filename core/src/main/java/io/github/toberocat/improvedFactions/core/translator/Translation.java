package io.github.toberocat.improvedFactions.core.translator;

import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.FileAccess;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public record Translation(@NotNull Locale locale) {

    private static final Map<String, Translatable> TRANSLATABLE_MAP = new HashMap<>();
    private static final Map<UUID, String> LANGUAGE_LOCALE_USAGE = new HashMap<>();
    private static final Map<String, String> LOCALE_TO_FILE_MAP = new HashMap<>();

    private static final FileAccess ACCESS = new FileAccess(ImprovedFactions.api().getLangFolder());

    /* Static manager methods */
    public static void createLocaleMap() throws IOException {
        for (File file : ACCESS.listFiles()) {
            for (String supported : YmlManager.read(Translatable.class, file).supportedLanguages())
                LOCALE_TO_FILE_MAP.put(supported, file.getName());
        }
    }

    public static void playerLeave(@NotNull FactionPlayer player) {
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

    private static @Nullable Translatable readFile(final @NotNull String _locale) {
        String locale = _locale.toLowerCase();
        String file = LOCALE_TO_FILE_MAP.get(locale);
        if (file == null) return null;

        File langFile = ACCESS.getFile(file);
        if (!langFile.exists()) return null;

        try {
            return YmlManager.read(Translatable.class, langFile);
        } catch (IOException e) {
            return null;
        }
    }

    public @Nullable String getMessage(@NotNull String query,
                                       @NotNull Map<String, Function<Translatable, String>> placeholders) {
        Translatable translatable = TRANSLATABLE_MAP.computeIfAbsent(locale.getCountry(),
                Translation::readFile);
        if (translatable == null) {
            Logger.api().logWarning("Couldn't find " + locale + " as lang file");
            return null;
        }
        Map<String, String> transformPlaceholders = transformPlaceholders(translatable, placeholders);
        System.out.println(query + "; Placeholders: " + transformPlaceholders);
        return StringUtils.replace(translatable.translations().get(query), transformPlaceholders);
    }

    public String[] getMessages(@NotNull String query,
                                @NotNull Map<String, Function<Translatable, String>> placeholders) {
        System.out.println("BATCH " + query);
        Translatable translatable = TRANSLATABLE_MAP.computeIfAbsent(locale.getCountry(),
                Translation::readFile);
        if (translatable == null) {
            Logger.api().logWarning("Couldn't find " + locale + " as lang file");
            return null;
        }

        List<String> list = new LinkedList<>();
        int i = 0;
        Map<String, String> transformPlaceholders = transformPlaceholders(translatable, placeholders);
        Map<String, String> translations = translatable.translations();
        while (translations.containsKey(query + "." + i)) {
            list.add(StringUtils.replace(translations.get(query + "." + i), transformPlaceholders));
            i++;
        }
        return list.toArray(String[]::new);
    }

    private @NotNull Map<String, String> transformPlaceholders(@NotNull Translatable translatable,
                                                               @NotNull Map<String, Function<Translatable, String>> placeholders) {
        return placeholders.entrySet()
                .stream()
                .map(x -> Map.entry(x.getKey(), x.getValue().apply(translatable)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
