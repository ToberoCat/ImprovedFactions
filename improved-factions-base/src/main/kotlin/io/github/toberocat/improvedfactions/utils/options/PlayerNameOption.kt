package io.github.toberocat.improvedfactions.utils.options

import org.bukkit.Bukkit


class PlayerNameOption(index: Int) : ArgumentOptions(index) {
    override fun validate(arg: String): Boolean = arg.isNotBlank() && Bukkit.getOfflinePlayer(arg)?.hasPlayedBefore() == true

    override fun argDoesntMatchKey(): String = "base.exceptions.arg-is-no-player"
}