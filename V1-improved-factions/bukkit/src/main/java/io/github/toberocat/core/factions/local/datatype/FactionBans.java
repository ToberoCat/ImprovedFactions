package io.github.toberocat.core.factions.local.datatype;

import io.github.toberocat.core.utility.data.annotation.TableKey;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@TableKey(key = "registry_id")
public record FactionBans(@NotNull String registry,
                          @NotNull UUID uuid) {
}
