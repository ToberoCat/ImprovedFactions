package io.github.toberocat.improvedfactions.modules.chat.commands

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.modules.chat.handles.ChatModuleHandle
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.entity.Player
import java.util.*

@CommandMeta(
    description = "chat.command.spy.description",
    module = "chat",
    category = CommandCategory.GENERAL_CATEGORY
)
class ChatSpyCommand(private val plugin: ImprovedFactionsPlugin,
                     private val chatHandler: ChatModuleHandle) : PlayerSubCommand("spy") {

    override fun options(): Options = Options.getFromConfig(plugin, label) {options, _ ->
    }

    override fun arguments(): Array<Argument<*>> = arrayOf()

    override fun handle(player: Player, args: Array<String>): Boolean {
        if (chatHandler.isSpying(player.uniqueId)) {
            chatHandler.unspy(player.uniqueId)
            player.sendLocalized("chat.command.spy.disabled")
        } else {
            chatHandler.spy(player.uniqueId)
            player.sendLocalized("chat.command.spy.enabled")
        }
        return true
    }
}