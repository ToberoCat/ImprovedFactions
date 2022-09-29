package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.item.Item;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public interface ItemHandler {

    static @NotNull ItemHandler api() {
        ItemHandler implementation = ImplementationHolder.itemHandler;
        if (implementation == null) throw new NoImplementationProvidedException("item handler");
        return implementation;
    }

    @NotNull ItemStack createStack(@NotNull String material, @NotNull String title, int quantity, String... lore);

    @NotNull ItemStack createSkull(@NotNull URL textureId, @NotNull String title, String... lore);

    @NotNull ItemStack fromBase64(@NotNull String data) throws IOException;

}
