package io.github.toberocat.improvedfactions.modules.chat.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.chat.ChatMode
import io.github.toberocat.improvedfactions.modules.chat.ChatModule
import io.github.toberocat.improvedfactions.modules.chat.ChatModule.sendFactionChat
import io.github.toberocat.improvedfactions.modules.chat.ChatModule.toggleChatMode
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.arguments.StringArgument
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player

@CommandMeta(
    description = "chat.commands.chat.description",
    module = ChatModule.MODULE_NAME
)
class ChatCommand(
    private val chatModuleHandle: ChatModuleHandle,
    private val plugin: ImprovedFactionsPlugin
) : PlayerSubCommand("chat") {
    override fun options(): Options = Options.getFromConfig(plugin, label) { options, _ ->
        options.cmdOpt(InFactionOption(true))
    }

    override fun arguments(): Array<Argument<*>> = arrayOf(
        StringArgument("[message]", "chat.commands.arg.chat-message")
    )

    override fun handle(player: Player, args: Array<String>): Boolean {
        if (args.isNotEmpty()) {
            player.sendFactionChat(args.joinToString(" "))
            return true
        }

        val nextMode = player.toggleChatMode()
        player.sendLocalized("chat.commands.chat.toggled-${nextMode.name.lowercase()}")
        return true
    }
}