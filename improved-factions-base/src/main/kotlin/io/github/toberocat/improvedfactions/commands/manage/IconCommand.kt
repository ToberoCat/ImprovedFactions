package io.github.toberocat.improvedfactions.commands.manage

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.improvedfactions.translation.sendLocalized
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.options.FactionPermissionOption
import io.github.toberocat.improvedfactions.utils.options.InFactionOption
import io.github.toberocat.improvedfactions.utils.options.IsFactionOwnerOption
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.exceptions.CommandException
import io.github.toberocat.toberocore.command.options.Options
import org.bukkit.Material
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    description = "base.command.icon.description",
    category = CommandCategory.MANAGE_CATEGORY
)
class IconCommand(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("icon") {
    override fun options(): Options = Options.getFromConfig(plugin, "icon") { options, _ ->
        options.opt(InFactionOption(true)).opt(IsFactionOwnerOption())
            .cmdOpt(FactionPermissionOption(Permissions.SET_ICON))
    }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<out String>): Boolean {
        val item = player.inventory.itemInMainHand.clone()
        if (item.type == Material.AIR) throw CommandException("base.command.icon.invalid-icon", emptyMap())

        transaction {
            val faction =
                player.factionUser().faction() ?: throw CommandException("base.command.icon.faction-needed", emptyMap())
            faction.icon = item
        }
        player.sendLocalized("base.command.icon.set-icon", emptyMap())
        return true
    }
}