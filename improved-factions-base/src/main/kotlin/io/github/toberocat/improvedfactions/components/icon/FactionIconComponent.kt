package io.github.toberocat.improvedfactions.components.icon

import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent
import io.github.toberocat.guiengine.function.GuiFunction
import io.github.toberocat.guiengine.render.RenderPriority
import io.github.toberocat.improvedfactions.factions.Faction

class FactionIconComponent(
    offsetX: Int,
    offsetY: Int,
    priority: RenderPriority,
    id: String,
    clickFunctions: List<GuiFunction>,
    dragFunctions: List<GuiFunction>,
    closeFunctions: List<GuiFunction>,
    faction: Faction,
    hidden: Boolean
) : SimpleItemComponent(
    offsetX, offsetY, priority, id, clickFunctions, dragFunctions, closeFunctions, faction.icon, hidden
)