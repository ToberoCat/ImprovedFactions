package io.github.toberocat.core.player;

import io.github.toberocat.core.utility.data.annotation.TableKey;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@TableKey(key = "uuid")
public record PlayerDataType(@NotNull UUID uuid,
                             @NotNull String faction,
                             @NotNull String memberRank) {
}
