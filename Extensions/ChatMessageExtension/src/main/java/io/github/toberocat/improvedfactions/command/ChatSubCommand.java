package io.github.toberocat.improvedfactions.command;

import io.github.toberocat.core.utility.command.SubCommand;
import io.github.toberocat.core.utility.command.SubCommandSettings;
import io.github.toberocat.improvedfactions.ChatMessageExtension;
import org.bukkit.entity.Player;

import java.util.List;

public class ChatSubCommand extends SubCommand {

    public ChatSubCommand() {
        super("chat", "command.chat.description", false);
    }

    @Override
    public SubCommandSettings getSettings() {
        return super.getSettings()
                .setAllowAliases(true)
                .setNeedsAdmin(false)
                .setArgLength(0)
                .setNeedsFaction(SubCommandSettings.NYI.Yes);
    }

    @Override
    protected void CommandExecute(Player player, String[] args) {
        ChatMessageExtension.changeRotationChatType(player);
    }

    @Override
    protected List<String> CommandTab(Player arg0, String[] arg1) {
        return null;
    }

}
