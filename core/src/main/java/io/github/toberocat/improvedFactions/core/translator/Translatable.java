package io.github.toberocat.improvedFactions.core.translator;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record Translatable(
        @NotNull String version,
        @NotNull @JsonProperty(namespace = "plugin-version") String pluginVersion,
        @NotNull String[] authors,
        @NotNull @JsonProperty(namespace = "supports") String[] supportedLanguages,
        @NotNull Map<String, String> translations,
        @NotNull Map<String, String> placeholders
) {
}
