package io.github.toberocat.improvedfactions.annotations.permission

enum class PermissionConfigurations(val ymlValue: String) {
    ALL("true"),
    OP_ONLY("op"),
    PLAYER_ONLY("not op"),
    NONE("false");

}