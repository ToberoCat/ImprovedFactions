package io.github.toberocat.improvedFactions.core.translator;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceholderBuilder {
    private final Map<String, Function<Translatable, String>> placeholders = new HashMap<>();

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull String value) {
        placeholders.put(key, translatable -> value);
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, int value) {
        return placeholder("{" + key + "}", String.valueOf(value));
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Rank value) {
        return placeholder("rank", key, value);
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Chunk value) {
        return placeholder("chunk", key, value);
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull OfflineFactionPlayer value) {
        return placeholder("offline-player", key, value);
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull FactionPlayer value) {
        return placeholder("player", key, value);
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Faction<?> value) {
        return placeholder("faction", key, value);
    }

    public Map<String, Function<Translatable, String>> getPlaceholders() {
        return placeholders;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String namespace,
                                                          @NotNull String key,
                                                          @NotNull Object value) {
        placeholders.put("{" + key + "}", translatable -> {
            String factionPlaceholder = translatable.placeholders().get(namespace);
            System.out.println(extractMethods(namespace, value));
            return StringUtils.replace(factionPlaceholder, extractMethods(namespace, value));
        });
        return this;
    }

    private @NotNull Map<String, String> extractMethods(@NotNull String namespace, @NotNull Object instance) {
        return Arrays.stream(instance.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(x -> "{" + namespace + "-" + x.getName() + "}", m -> {
                    try {
                        return String.valueOf(m.invoke(instance));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException ignored) {
                        return "invalid";
                    }
                }, (o, n) -> o));
    }
}
