package io.github.toberocat.improvedfactions.modules.wilderness.config

data class Region(
    var minX: Int = 0,
    var maxX: Int = 0,
    var minZ: Int = 0,
    var maxZ: Int = 0,
    var world: String = ""
)