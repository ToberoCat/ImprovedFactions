package io.github.toberocat.improvedfactions.action

import io.github.toberocat.guiengine.utils.GuiEngineAction
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.translation.getUnformattedLocalized
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.utils.async
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.input.ChatInput
import io.github.toberocat.toberocore.task.Promise
import org.bukkit.entity.Player
import java.util.function.Function

class CommandActionMapper(private val label: String, private val command: PlayerSubCommand) : GuiEngineAction() {
    override fun label(): String = label


    override fun run(player: Player) {
        async {
            val arguments = command.args.map {
                (Promise { resolve ->
                    player.sendLocalized(
                        "base.action.required-argument", mapOf(
                            "usage" to it.usage(),
                            "description" to player.getUnformattedLocalized(it.descriptionKey())
                        )
                    )
                    player.sendLocalized(
                        "base.action.possible-values", mapOf(
                            "values" to it.tab(player)?.toTypedArray().contentToString(),
                        )
                    )
                    ChatInput.prompt(ImprovedFactionsPlugin.instance, player, null, Function { text ->

                        val result: Any?
                        try {
                            result = it.parse(player, text)
                        } catch (e: CommandException) {
                            player.sendLocalized(e.localizedMessage, e.placeholders)
                            player.sendLocalized("base.action.try-again")
                            return@Function ChatInput.Action.RETRY
                        }

                        if (result == null) {
                            player.sendLocalized("base.action.try-again")
                            return@Function ChatInput.Action.RETRY
                        }
                        resolve.accept(text)
                        return@Function ChatInput.Action.SUCCESS
                    })
                }).result()
            }.toMutableList()

            arguments.add(0, "")
            try {
                command.routeCall(player, arguments.toTypedArray())
            } catch (e: CommandException) {
                player.sendLocalized(e.localizedMessage, e.placeholders)
            }
        }
    }
}