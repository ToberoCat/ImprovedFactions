package io.github.toberocat.improvedfactions.components.rank

import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import io.github.toberocat.improvedfactions.ranks.FactionRank
import io.github.toberocat.toberocore.util.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


fun createItem(rank: FactionRank): ItemStack {
    val material = Material.entries[rank.id.value % Material.entries.size]

    val countUsers = rank.countAssignedUsers()
    val userPreviewList = rank.getAssignedUserPreview(5)
        .map { "  §e- ${it.name}" }
    val preview = mutableListOf<String>()
    if (userPreviewList.isNotEmpty()) preview.add("§7Users assigned:")
    preview.addAll(userPreviewList)
    if (countUsers > userPreviewList.size)
        preview.add("  §e- ${countUsers - userPreviewList.size} Users left")

    return ItemBuilder()
        .material(material)
        .title("§e${rank.name}")
        .lore(
            "",
            "§7Priority: §e${rank.priority}",
            "§7Assigned Users: §e${countUsers}",
            *preview.toTypedArray()
        )
        .create(ImprovedFactionsPlugin.instance)
}

class FactionRankComponent(
    offsetX: Int, offsetY: Int, priority: RenderPriority, id: String,
    clickFunctions: List<GuiFunction>, dragFunctions: List<GuiFunction>,
    closeFunctions: List<GuiFunction>, rank: FactionRank, hidden: Boolean
) : SimpleItemComponent(
    offsetX, offsetY,
    priority,
    id, clickFunctions, dragFunctions, closeFunctions, createItem(rank), hidden
)