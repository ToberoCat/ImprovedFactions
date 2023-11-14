package io.github.toberocat.improvedfactions.utils.options

import org.bukkit.Bukkit

class PlayerNameOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = Bukkit.getPlayer(arg) != null

    override fun argDoesntMatchKey(): String = "base.exceptions.arg-is-no-player"
}