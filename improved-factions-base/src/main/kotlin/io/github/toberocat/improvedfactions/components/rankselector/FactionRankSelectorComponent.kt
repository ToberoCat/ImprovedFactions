package io.github.toberocat.improvedfactions.components.rankselector

import io.github.toberocat.guiengine.components.AbstractGuiComponent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.database.DatabaseManager.loggedTransaction
import io.github.toberocat.improvedfactions.ranks.listRanks
import io.github.toberocat.improvedfactions.user.factionUser
import io.github.toberocat.toberocore.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack


private const val SELECTED_TEXT = "  §8[§e*§8] §f"
private const val UNSELECTED_TEXT = "      §f"

class FactionRankSelectorComponent(
    private val player: OfflinePlayer,
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
        println(loggedTransaction { player.factionUser().assignedRank })
        selected = loggedTransaction { getSelectionModel().indexOf(player.factionUser().rank().name) }
    }

    override fun clickedComponent(event: InventoryClickEvent) {
        super.clickedComponent(event)
        setSelected(selected + 1)
    }

    override fun render(player: Player, buffer: Array<Array<ItemStack>>) {
        val selectionModelLore = mutableListOf<String>()
        getSelectionModel().forEachIndexed { index, s ->
            println(index)
            println(selected)
            selectionModelLore.add(
                if (index == selected)
                    SELECTED_TEXT + s else UNSELECTED_TEXT + s
            )
        }
        buffer[offsetY][offsetX] = ItemBuilder()
            .title("§eRank")
            .material(Material.BOOK)
            .lore(
                "",
                "§7Click to toggle",
                *selectionModelLore.toTypedArray()
            ).create(ImprovedFactionsPlugin.instance)
    }

    fun getSelectionModel(): Array<String> = loggedTransaction {
        println(player.factionUser().faction()
            ?.listRanks()
            ?.map { it.name })
        player.factionUser().faction()
            ?.listRanks()
            ?.map { it.name }
            ?.toTypedArray()
    }
        ?: emptyArray<String>()

    fun getSelected(): Int = selected

    fun setSelected(value: Int) {
        selected = value % getSelectionModel().size
        loggedTransaction {
            val user = player.factionUser()
            user.assignedRank = user.faction()
                ?.listRanks()
                ?.toList()
                ?.get(selected)
                ?.id?.value ?: user.assignedRank
        }
    }

    companion object {
        const val TYPE = "faction-rank-selector"
    }
}