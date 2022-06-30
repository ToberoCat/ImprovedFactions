package io.github.toberocat.core.utility.gui;

import io.github.toberocat.core.utility.gui.builder.GuiBuilder;
import io.github.toberocat.core.utility.gui.builder.GuiBuilderSerializer;
import io.github.toberocat.core.utility.gui.builder.Placeholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfigGui {

    protected void createFromSection(@NotNull ConfigurationSection section,
                                     @NotNull Player player,
                                     @NotNull Placeholder placeholder) {
        GuiBuilderSerializer serializer = new GuiBuilderSerializer(section, placeholder.placeholders());
        GuiBuilder builder = new GuiBuilder();

        int rows = serializer.readInt("rows", 1);
        builder.title(serializer.readString("title", null));
        builder.arrows(serializer.readBool("arrows", false));
        builder.quit(serializer.readList("quit", null));

    }
}
