package io.github.toberocat.improvedfactions.commands.claim

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.annotations.command.CommandCategory
import io.github.toberocat.improvedfactions.annotations.command.CommandResponse
import io.github.toberocat.improvedfactions.annotations.command.GeneratedCommandMeta
import io.github.toberocat.improvedfactions.database.storage.ClaimSnapshot
import io.github.toberocat.improvedfactions.database.storage.StorageManager
import io.github.toberocat.improvedfactions.commands.CommandProcessResult
import io.github.toberocat.improvedfactions.modules.base.BaseModule
import io.github.toberocat.improvedfactions.translation.getLocalized
import io.github.toberocat.improvedfactions.utils.appendIf
import io.github.toberocat.improvedfactions.utils.toAudience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.entity.Player

@GeneratedCommandMeta(
    label = "map",
    category = CommandCategory.CLAIM_CATEGORY,
    module = "base",
    responses = [
        CommandResponse("mapRendered"),
        CommandResponse("noClaimsNearby")
    ]
)
abstract class FactionMapCommand : FactionMapCommandContext() {

    val width get() = BaseModule.config.mapWidth
    val height get() = BaseModule.config.mapHeight

    fun process(player: Player): CommandProcessResult {
        val claims = getAffectedClaims(player)
        if (claims.isEmpty()) {
            return noClaimsNearby()
        }

        val audience = player.toAudience()
        val header = ".[ Faction Map ]."
        val padding = "_".repeat(width - header.length / 3 - 1)

        audience.sendMessage(
            Component.text()
                .append(Component.text(padding).color(NamedTextColor.DARK_GRAY))
                .append(Component.text(header).color(NamedTextColor.YELLOW))
                .append(Component.text(padding).color(NamedTextColor.DARK_GRAY))
                .append(Component.newline())
        )

        for (x in -height..height) {
            val mapBuffer = Component.text()
            for (z in -width..width) {
                val chunkKey = getCombined(player.location.chunk.x + x, player.location.chunk.z + z)
                val claim = claims[chunkKey]

                var component = Component.text("-").color(NamedTextColor.GRAY)
                claim?.let {
                    component = createClaimComponent(player, claim)
                }

                if (x == 0 && z == 0) {
                    component = Component.text("+")
                        .color(NamedTextColor.YELLOW)
                        .hoverEvent(HoverEvent.showText(Component.text("You").color(NamedTextColor.YELLOW)))
                }

                mapBuffer.append(component)
            }
            audience.sendMessage(mapBuffer)
        }

        return mapRendered()
    }

    private fun createClaimComponent(player: Player, claim: ClaimSnapshot): TextComponent {
        return if (claim.factionId != io.github.toberocat.improvedfactions.user.noFactionId) {
            StorageManager.cache.faction(claim.factionId)?.let {
                val color = TextColor.color(io.github.toberocat.improvedfactions.factions.FactionHandler.generateColor(it.id))
                val hoverComponent = Component.text(it.name)
                    .color(color)
                    .appendIf(claim.isRaidable) {
                        Component.text(" (Raidable)").color(NamedTextColor.RED)
                    }
                Component.text("#").color(color).hoverEvent(hoverComponent)
            } ?: Component.text("-").color(NamedTextColor.GRAY)
        } else {
            io.github.toberocat.improvedfactions.zone.ZoneHandler.getZone(claim.zoneType)?.let {
                Component.text("/")
                    .color(TextColor.color(it.mapColor))
                    .hoverEvent(HoverEvent.showText(player.getLocalized(it.noFactionTitle)))
            } ?: Component.text("-").color(NamedTextColor.GRAY)
        }
    }

    private fun getAffectedClaims(player: Player): Map<Long, ClaimSnapshot> {
        val chunk = player.location.chunk
        return StorageManager.cache.claimsNear(player.world.name, chunk.x, chunk.z, maxOf(width, height))
            .filter { kotlin.math.abs(it.key.chunkX - chunk.x) <= height && kotlin.math.abs(it.key.chunkZ - chunk.z) <= width }
            .associateBy { getCombined(it.key.chunkX, it.key.chunkZ) }
    }

    private fun getCombined(x: Int, z: Int): Long = x.toLong() shl 32 or (z.toLong() and 0xFFFFFFFFL)
}
