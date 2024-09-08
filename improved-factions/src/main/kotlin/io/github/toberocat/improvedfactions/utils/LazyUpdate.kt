package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Bukkit
import java.util.concurrent.TimeUnit

class LazyUpdate<T>(private val compute: () -> T) {
    private var cached: T? = null
    private var lastAccess = System.currentTimeMillis()

    init {
        scheduleUpdateInterval()
    }

    fun get(): T {
        if (cached == null) {
            forceUpdate()
        }
        lastAccess = System.currentTimeMillis()
        return cached!!
    }

    fun scheduleUpdate() {
        cached = null
    }

    private fun forceUpdate() {
        cached = compute()
    }

    // This method makes sure the cache will clear after 15 minutes of inactivity, freeing up memory
    private fun scheduleUpdateInterval() {
        Bukkit.getScheduler().runTaskLater(ImprovedFactionsPlugin.instance, Runnable {
            if (System.currentTimeMillis() - lastAccess > TimeUnit.MINUTES.toMillis(15)) {
                scheduleUpdate()
            }
            scheduleUpdateInterval()
        }, 20L * TimeUnit.MINUTES.toSeconds(15))
    }
}