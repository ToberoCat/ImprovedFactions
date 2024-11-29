package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.claims.FactionClaim
import io.github.toberocat.improvedfactions.claims.FactionClaims
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandMeta
import io.github.toberocat.improvedfactions.utils.toAudience
import io.github.toberocat.toberocore.command.PlayerSubCommand
import io.github.toberocat.toberocore.command.arguments.Argument
import io.github.toberocat.toberocore.command.options.Options
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.and

@CommandMeta(
    category = CommandCategory.CLAIM_CATEGORY,
    description = "base.command.faction-map.description"
)
class FactionMap(private val plugin: ImprovedFactionsPlugin) : PlayerSubCommand("map") {
    override fun options() = Options.getFromConfig(plugin, "map")

    override fun arguments(): Array<Argument<*>> = emptyArray()

    override fun handle(player: Player, args: Array<String>): Boolean {
        val header = ".[ Faction Map ]."
        val padding = "_".repeat(MAP_WIDTH - header.length / 3 - 1)
        val audience = player.toAudience()
        audience.sendMessage(
            Component.text()
                .append(
                    Component.text(padding)
                        .color(NamedTextColor.DARK_GRAY)
                )

                .append(
                    Component.text(header)
                        .color(NamedTextColor.YELLOW)
                )

                .append(
                    Component.text(padding)
                        .color(NamedTextColor.DARK_GRAY)
                )
                .append(Component.newline())
        )

        val claims = getAffectedClaims(player)
        loggedTransaction {
            for (x in -MAP_HEIGHT..MAP_HEIGHT) {
                val mapBuffer = Component.text()
                for (z in -MAP_WIDTH..MAP_WIDTH) {
                    val claim = claims[getCombined(player.location.chunk.x + x, player.location.chunk.z + z)]

                    var component = Component.text("-")
                        .color(NamedTextColor.GRAY)
                    when {
                        claim == null || !claim.isClaimed() -> claim?.zone()?.let {
                            if (it.type != "default") {
                                component = component.content("/")
                            }
                            component = component.color(TextColor.color(it.mapColor))
                                .hoverEvent(HoverEvent.showText(player.getLocalized(it.noFactionTitle)))
                        }

                        claim.isClaimed() -> claim.faction()?.let {
                            val color = TextColor.color(it.generateColor())
                            component = component.color(color)
                                .content("#")
                            var hoverComponent = Component.text(it.name)
                                .color(color)
                            if (claim.isRaidable() == true) {
                                hoverComponent =
                                    hoverComponent.append(Component.text(" (Raidable)").color(NamedTextColor.RED))
                            }

                            component = component.hoverEvent(hoverComponent)
                        }
                    }

                    if (x == 0 && z == 0) {
                        component = component.color(NamedTextColor.YELLOW)
                            .content("+")
                            .hoverEvent(
                                HoverEvent.showText(
                                    Component.text("You")
                                        .color(NamedTextColor.YELLOW)
                                )
                            )
                    }

                    mapBuffer.append(component)
                }
                audience.sendMessage(mapBuffer)
            }
        }

        return true
    }

    private fun getAffectedClaims(player: Player): Map<Long, FactionClaim> {
        val lowerZ = player.location.chunk.z - MAP_WIDTH
        val upperZ = player.location.chunk.z + MAP_WIDTH
        val lowerX = player.location.chunk.x - MAP_HEIGHT
        val upperX = player.location.chunk.x + MAP_HEIGHT
        return loggedTransaction {
            FactionClaim.find { FactionClaims.chunkZ greaterEq lowerZ and (FactionClaims.chunkZ lessEq upperZ) and (FactionClaims.chunkX greaterEq lowerX) and (FactionClaims.chunkX lessEq upperX) }
                .associateBy { getCombined(it.chunkX, it.chunkZ) }
        }
    }

    private fun getCombined(x: Int, z: Int) = x.toLong() shl 32 or (z.toLong() and 0xFFFFFFFFL)

    companion object {
        var MAP_WIDTH = 20
        var MAP_HEIGHT = 10
    }
}