package io.github.toberocat.improvedFactions.core.permission;

import org.jetbrains.annotations.NotNull;

public enum FactionPermission implements Permission {
    CLAIM_PERMISSION("claim-chunk-permission"),
    UNCLAIM_PERMISSION("unclaim-chunk-permission"),
    BREAK_PERMISSION("break-block-permission"),
    PLACE_PERMISSION("place-block-permission"),
    OPEN_SETTINGS_PERMISSION("open-settings-permission"),
    RENAME_FACTION("rename-permission");

    final @NotNull String name;

    FactionPermission(@NotNull String name) {
        this.name = name;
    }

    @Override
    public @NotNull String label() {
        return name;
    }
}
