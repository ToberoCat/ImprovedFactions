package io.github.toberocat.improvedfactions.utils

class LazyUpdate<T>(default: T, private val compute: () -> T) {
    private var lazyUpdate = true
    private var cached: T = default

    fun get(): T {
        if (lazyUpdate) {
            forceUpdate()
        }
        return cached
    }

    fun scheduleUpdate() {
        lazyUpdate = true
    }

    private fun forceUpdate() {
        lazyUpdate = false
        cached = compute()
    }
}