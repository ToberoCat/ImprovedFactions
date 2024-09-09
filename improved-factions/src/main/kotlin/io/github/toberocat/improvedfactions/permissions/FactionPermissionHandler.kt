package io.github.toberocat.improvedfactions.permissions

import org.bukkit.Material

object Permissions {
    val knownPermissions: MutableMap<String, PermissionHolder> = mutableMapOf()

    val VIEW_POWER = "view-power".registerAsPermission(Material.BEACON)
    val MANAGE_CLAIMS = "manage-claims".registerAsPermission(Material.DIRT)
    val SEND_INVITES = "send-invites".registerAsPermission(Material.BIRCH_SIGN)
    val SET_ICON = "set-icon".registerAsPermission(Material.WHITE_BANNER)
    val RENAME_FACTION = "rename-faction".registerAsPermission(Material.NAME_TAG)
    val MANAGE_PERMISSIONS = "manage-permissions".registerAsPermission(Material.REDSTONE)
    val KICK_PLAYER = "kick-player".registerAsPermission(Material.WOODEN_SWORD)
    val MANAGE_BANS = "manage-bans".registerAsPermission(Material.NETHERITE_AXE)
    val TRANSFER_OWNERSHIP = "transfer-ownership".registerAsPermission(Material.BARRIER)
    val SET_HOME = "set-home".registerAsPermission(Material.RED_BED)
    val HOME = "teleport-home".registerAsPermission(Material.WHITE_BED)
    val SET_JOIN_TYPE = "set-join-type".registerAsPermission(Material.IRON_DOOR)
    val MANAGE_RELATION = "manage-relation".registerAsPermission(Material.IRON_SWORD)

    fun String.registerAsPermission(material: Material): String {
        knownPermissions[this] = PermissionHolder(material)
        return this
    }
}

data class PermissionHolder(val material: Material)