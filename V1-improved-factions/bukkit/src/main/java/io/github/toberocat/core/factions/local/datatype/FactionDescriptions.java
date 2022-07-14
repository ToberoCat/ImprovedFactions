package io.github.toberocat.core.factions.local.datatype;

import io.github.toberocat.core.utility.data.annotation.TableKey;
import org.jetbrains.annotations.NotNull;

@TableKey(key = "registry_id")
public record FactionDescriptions(@NotNull String registry,
                                  @NotNull String content,
                                  int line) {
}
