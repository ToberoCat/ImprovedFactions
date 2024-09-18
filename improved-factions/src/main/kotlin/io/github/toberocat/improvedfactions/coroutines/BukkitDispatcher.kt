package io.github.toberocat.improvedfactions.coroutines

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import org.bukkit.Bukkit
import kotlin.coroutines.CoroutineContext

class BukkitDispatcher(private val plugin: ImprovedFactionsPlugin) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        Bukkit.getScheduler().runTask(plugin, block)
    }
}