package io.github.toberocat.improvedfactions.utils.options

import io.github.toberocat.improvedfactions.utils.hasOfflinePlayerByName


class PlayerNameOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = arg.isNotBlank() && arg.hasOfflinePlayerByName()

    override fun argDoesntMatchKey(): String = "base.exceptions.arg-is-no-player"
}