package io.github.toberocat.improvedfactions.spigot.utils;

import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created: 22/12/2022
 *
 * @author Tobias Madlberger (Tobias)
 */
public class ComponentUtility {
    public static @NotNull Component create(@Nullable String text, Object... o) {
        if (text == null)
            return Component.empty();
        return MiniMessage.miniMessage().deserialize(String.format(text, o));
    }

    public static @NotNull Component format(@NotNull String text, Object... o) {
        return ComponentUtility.create(MessageHandler.api().format(text), o);
    }
}
