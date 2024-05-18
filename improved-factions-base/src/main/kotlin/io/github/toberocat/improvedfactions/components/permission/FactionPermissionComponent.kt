package io.github.toberocat.improvedfactions.components.permission

import io.github.toberocat.guiengine.components.AbstractGuiComponent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.permissions.FactionPermission
import io.github.toberocat.improvedfactions.permissions.Permissions
import io.github.toberocat.toberocore.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

const val TYPE = "faction-permission"

private const val SELECTED_TEXT = "  §8[§e*§8] §f"
private const val UNSELECTED_TEXT = "      §f"

class FactionPermissionComponent(
    private val permission: FactionPermission,
    offsetX: Int,
    offsetY: Int,
    width: Int,
    height: Int,
    priority: RenderPriority,
    id: String,
    clickFunctions: List<GuiFunction>,
    dragFunctions: List<GuiFunction>,
    closeFunctions: List<GuiFunction>,
    hidden: Boolean
) : AbstractGuiComponent(
    offsetX, offsetY, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden
) {
    private var selected: Int = 0
    override val type = TYPE

    init {
        selected = if (permission.allowed) 0 else 1
    }

    override fun clickedComponent(event: InventoryClickEvent) {
        super.clickedComponent(event)
        setSelected(selected xor 1)
    }

    override fun render(player: Player, buffer: Array<Array<ItemStack>>) {
        buffer[offsetY][offsetX] = ItemBuilder()
            .title("§e${permission.permission}")
            .material(Permissions.knownPermissions[permission.permission]?.material ?: Material.BARRIER)
            .lore(
                "",
                "§7Click to toggle",
                if (selected == 0) SELECTED_TEXT + selectionModel[0] else UNSELECTED_TEXT + selectionModel[0],
                if (selected == 1) SELECTED_TEXT + selectionModel[1] else UNSELECTED_TEXT + selectionModel[1],
            ).create(ImprovedFactionsPlugin.instance)
    }

    private val selectionModel = arrayOf("§aAllow", "§cDeny")

    fun getSelected(): Int = selected

    private fun setSelected(value: Int) {
        selected = value
        loggedTransaction { permission.allowed = value == 0 }
    }
}