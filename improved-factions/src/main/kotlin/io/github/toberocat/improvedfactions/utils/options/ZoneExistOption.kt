package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.zone.ZoneHandler

class ZoneExistOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = ZoneHandler.hasZone(arg)

    override fun argDoesntMatchKey(): String = "base.exception.zone-doesnt-exist"
}