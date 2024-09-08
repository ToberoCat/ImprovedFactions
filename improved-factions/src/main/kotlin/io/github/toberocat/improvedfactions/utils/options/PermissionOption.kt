package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.permissions.Permissions

class PermissionOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = Permissions.knownPermissions.contains(arg)

    override fun argDoesntMatchKey(): String = "base.exceptions.permission-not-valid"
}