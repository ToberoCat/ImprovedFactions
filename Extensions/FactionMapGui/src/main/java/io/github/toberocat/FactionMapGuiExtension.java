package io.github.toberocat;

import io.github.toberocat.command.MapGuiSubCommand;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.extensions.Extension;


public class FactionMapGuiExtension extends Extension {

    @Override
    protected void onEnable(MainIF plugin) {
        FactionCommand.subCommands.removeIf(x -> x.getSubCommand().equals("map"));
        FactionCommand.subCommands.add(new MapGuiSubCommand());
    }
}
