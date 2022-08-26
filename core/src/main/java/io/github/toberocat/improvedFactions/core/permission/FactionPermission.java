package io.github.toberocat.improvedFactions.core.permission;

import org.jetbrains.annotations.NotNull;

public enum FactionPermission implements Permission {
    CLAIM_PERMISSION("claim-permission");

    final @NotNull String name;

    FactionPermission(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull String label() {
        return name;
    }
}
