package io.github.toberocat.improvedFactions.core.translator;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record Translatable(
        @NotNull @JsonProperty("version") String version,
        @NotNull @JsonProperty("plugin-version") String pluginVersion,
        @NotNull @JsonProperty("authors") String[] authors,
        @NotNull @JsonProperty("supports") String[] supportedLanguages,
        @NotNull @JsonProperty("translations") Map<String, String> translations,
        @NotNull @JsonProperty("placeholders") Map<String, String> placeholders
) {
}
