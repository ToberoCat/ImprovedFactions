package io.github.toberocat.improvedFactions.core.item;

import org.jetbrains.annotations.NotNull;

public interface ItemStack {
    @NotNull String toBase64();

    @NotNull Object getRaw();

    void setName(@NotNull String name);

    void setLore(@NotNull String... lore);
}
