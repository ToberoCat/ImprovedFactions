package io.github.toberocat.core.player;

import io.github.toberocat.core.utility.data.annotation.TableKey;
import org.jetbrains.annotations.NotNull;


@TableKey(key = "uuid")
public record PlayerSettingDataType(@NotNull String registry,
                                    @NotNull String setting,
                                    @NotNull String value) {
}
