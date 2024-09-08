package io.github.toberocat.improvedfactions.utils

import io.github.toberocat.improvedfactions.ImprovedFactionsPlugin
import org.bukkit.Bukkit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val threadPool: ExecutorService = Executors.newFixedThreadPool(
    ImprovedFactionsPlugin.instance
        .config.getInt("performance.thread-pool-size", 10)
)

fun async(task: () -> Unit) {
    threadPool.submit(task)
}

fun sync(task: () -> Unit) {
    Bukkit.getScheduler().runTask(ImprovedFactionsPlugin.instance, task)
}

