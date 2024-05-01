package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.getFactionClaim
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.translation.getUnformattedLocalized
import io.github.toberocat.improvedfactions.user.noFactionId
import io.github.toberocat.improvedfactions.utils.command.CommandCategory
import io.github.toberocat.improvedfactions.utils.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.transactions.transaction

@CommandMeta(
    category = CommandCategory.CLAIM_CATEGORY,
    description = "base.command.faction-map.description"
)
class FactionMap(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("map") {
    override fun options() = Options.getFromConfig(plugin, "map") { options, _ -> options }

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<String>): Boolean {
        val padding = "_".repeat(MAP_SIZE)
        val mapBuffer = Component.text()
            .append(
                Component.text(padding)
                    .color(NamedTextColor.DARK_GRAY)
            )

            .append(
                Component.text(".[ Faction Map ].")
                    .color(NamedTextColor.YELLOW)
            )

            .append(
                Component.text(padding)
                    .color(NamedTextColor.DARK_GRAY)
            )
            .append(Component.newline())

        transaction {
            for (x in -MAP_SIZE..MAP_SIZE) {
                for (z in -MAP_SIZE..MAP_SIZE) {
                    val claim = player.world
                        .getChunkAt(player.location.chunk.x + x, player.location.chunk.z + z)
                        .getFactionClaim()

                    var component = Component.text("-")
                        .color(NamedTextColor.GRAY)
                    if (claim == null || claim.canClaim()) {
                        claim?.zone()?.let {
                            if (it.type != "default") {
                                component = component.content("/")
                            }
                            component = component.color(TextColor.color(it.mapColor))
                                .hoverEvent(HoverEvent.showText(player.getLocalized(it.noFactionTitle)))
                        }
                    } else if (claim.factionId != noFactionId) {
                        claim.faction()?.let {
                            component = component.color(TextColor.color(it.generateColor()))
                                .content("#")
                            var hoverComponent = Component.text(it.name)
                            if (claim.isRaidable() == true) {
                                hoverComponent = hoverComponent.append(Component.text(" (Raidable)").color(NamedTextColor.RED))
                            }

                            component = component.hoverEvent(hoverComponent)
                        }
                    }

                    if (x == 0 && z == 0) {
                        component = component.color(NamedTextColor.YELLOW)
                            .content("+")
                    }

                    mapBuffer.append(component)
                }
                mapBuffer.append(Component.newline())
            }
        }

        player.toAudience().sendMessage(mapBuffer)
        return true
    }

    companion object {
        var MAP_SIZE = 10
    }
}