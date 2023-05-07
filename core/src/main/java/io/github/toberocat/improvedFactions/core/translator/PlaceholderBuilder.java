package io.github.toberocat.improvedFactions.core.translator;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlaceholderBuilder {
    private final Map<String, Function<Translatable, String>> placeholders = new HashMap<>();

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull String value) {
        placeholders.put(key, translatable -> value);
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Rank value) {
        Map<String, String> methods = Arrays.stream(value.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(Method::getName, m -> {
                    try {
                        return "rank-" + m.invoke(value).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
        placeholders.put(key, translatable -> {
            String factionPlaceholder = translatable.placeholders().get("rank");
            return StringUtils.replace(factionPlaceholder, methods);
        });
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Chunk value) {
        Map<String, String> methods = Arrays.stream(value.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(Method::getName, m -> {
                    try {
                        return "chunk-" + m.invoke(value).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
        placeholders.put(key, translatable -> {
            String factionPlaceholder = translatable.placeholders().get("chunk");
            return StringUtils.replace(factionPlaceholder, methods);
        });
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull OfflineFactionPlayer value) {
        Map<String, String> methods = Arrays.stream(value.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(Method::getName, m -> {
                    try {
                        return "offlineplayer-" + m.invoke(value).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
        placeholders.put(key, translatable -> {
            String factionPlaceholder = translatable.placeholders().get("offlineplayer");
            return StringUtils.replace(factionPlaceholder, methods);
        });
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull FactionPlayer value) {
        Map<String, String> methods = Arrays.stream(value.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(Method::getName, m -> {
                    try {
                        return "player-" + m.invoke(value).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }, (o, n) -> o));
        System.out.println(methods);
        placeholders.put(key, translatable -> {
            String factionPlaceholder = translatable.placeholders().get("player");
            return StringUtils.replace(factionPlaceholder, methods);
        });
        return this;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, @NotNull Faction<?> value) {
        Map<String, String> methods = Arrays.stream(value.getClass().getMethods())
                .filter(x -> x.getParameterCount() == 0)
                .collect(Collectors.toMap(Method::getName, m -> {
                    try {
                        return "faction-" + m.invoke(value).toString();
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }));
        placeholders.put(key, translatable -> {
            String factionPlaceholder = translatable.placeholders().get("faction");
            return StringUtils.replace(factionPlaceholder, methods);
        });
        return this;
    }

    public Map<String, Function<Translatable, String>> getPlaceholders() {
        return placeholders;
    }

    public @NotNull PlaceholderBuilder placeholder(@NotNull String key, int value) {
        return placeholder(key, String.valueOf(value));
    }
}
