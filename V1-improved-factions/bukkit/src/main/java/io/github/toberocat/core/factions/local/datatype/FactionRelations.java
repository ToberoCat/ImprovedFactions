package io.github.toberocat.core.factions.local.datatype;

import io.github.toberocat.core.utility.data.annotation.TableKey;
import org.jetbrains.annotations.NotNull;

@TableKey(key = "registry_id")
public record FactionRelations(@NotNull String registry,
                               @NotNull String relationRegistry,
                               @NotNull RelationStatus status) {
}
