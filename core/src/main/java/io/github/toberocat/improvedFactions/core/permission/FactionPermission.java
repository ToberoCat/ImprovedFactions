package io.github.toberocat.improvedFactions.core.permission;

import org.jetbrains.annotations.NotNull;

public enum FactionPermission implements Permission {
    CLAIM_PERMISSION("claim-chunk-permission"),
    BREAK_PERMISSION("break-block-permission");

    final @NotNull String name;

    FactionPermission(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull String label() {
        return name;
    }
}
