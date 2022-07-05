package io.github.toberocat;

import io.github.toberocat.command.ClaimGuiSubCommand;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.extensions.Extension;


public class FactionClaimGuiExtension extends Extension {

    @Override
    protected void onEnable(MainIF plugin) {
        FactionCommand.subCommands.removeIf(x -> x.getSubCommand().equals("claim"));
        FactionCommand.subCommands.add(new ClaimGuiSubCommand());
    }
}
